package com.atguigu.gulimall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.to.mq.SeckillOrderTo;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberResponseVO;
import com.atguigu.gulimall.seckill.fegin.CouponFeignService;
import com.atguigu.gulimall.seckill.fegin.ProductFeignService;
import com.atguigu.gulimall.seckill.interceptor.LoginUserInterceptor;
import com.atguigu.gulimall.seckill.service.SeckillService;
import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;
import com.atguigu.gulimall.seckill.vo.SeckillSessionWithSkus;
import com.atguigu.gulimall.seckill.vo.SkuInfoVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description: SeckillServiceImpl
 * @Author: WangTianShun
 * @Date: 2020/12/13 9:51
 * @Version 1.0
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";//+商品随机码

    @Override
    public void uploadSeckillSkuLatest3Days() {
        // 1、扫描最近三天需要参与秒杀的活动
        R session = couponFeignService.getLasts3DaySession();
        if (session.getCode() == 0){
            // 上架商品
            List<SeckillSessionWithSkus> data = session.getData(new TypeReference<List<SeckillSessionWithSkus>>() {
            });
            // 缓存到redis
            System.out.println("data---------------------"+data);
            // 1、缓存活动信息
            saveSessionInfos(data);

            // 2、缓存获得关联商品信息
            saveSessionSkuInfos(data);
        }
    }

    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        long currentTime = System.currentTimeMillis();
        for (String key : keys) {
            String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
            String[] split = replace.split("_");
            long startTime = Long.parseLong(split[0]);
            long endTime = Long.parseLong(split[1]);
            // 当前秒杀活动处于有效期内
            if (currentTime > startTime && currentTime < endTime) {
                // 获取这个秒杀场次的所有商品信息
                List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                assert range != null;
                List<String> strings = hashOps.multiGet(range);
                if (!CollectionUtils.isEmpty(strings)) {
                    return strings.stream().map(item -> JSON.parseObject(item, SeckillSkuRedisTo.class))
                            .collect(Collectors.toList());
                }
                break;
            }
        }
        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSecKillInfo(Long skuId) {
        // 1、找到所有需要参与秒杀的商品的key
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (null != keys){
            //6_4
           String regx = "\\d_" + skuId;
            for (String key : keys) {
                if (Pattern.matches(regx, key)){
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo skuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);

                    //随机码
                    long current = new Date().getTime();
                    if (current >= skuRedisTo.getStartTime() && current <= skuRedisTo.getEndTime()){

                    }else {
                        skuRedisTo.setRandomCode(null);
                    }
                    return skuRedisTo;
                }

            }
        }
        return null;
    }
    // TODO 上架秒杀商品的时候，每一个数据都有一个过期时间。
    // TODO 秒杀后续的流程，简化了 收货地址等信息。
    @Override
    public String kill(String killId, String key, Integer num) {
        long s1 = System.currentTimeMillis();
        // 从拦截器获取用户信息
        MemberResponseVO repsVo = LoginUserInterceptor.loginUser.get();
        // 1、获取当前商品的详细信息
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = hashOps.get(killId);
        if (!StringUtils.isEmpty(json)){
            SeckillSkuRedisTo redis = JSON.parseObject(json, SeckillSkuRedisTo.class);
            // 校验合法性
            Long startTime = redis.getStartTime();
            Long endTime = redis.getEndTime();
            long current = new Date().getTime();
            long ttl = endTime - startTime; //场次存活时间
            // 1.1校验时间的合法性
            if (current >= startTime && current <= endTime){
                // 1.2校验随机码和商品id
                String randomCode = redis.getRandomCode();
                String skuId = redis.getPromotionSessionId() + "_" + redis.getSkuId();
                if (randomCode.equals(key) && skuId.equals(killId)){
                    // 1.3、验证购物的数量是否合理
                    if (num <= redis.getSeckillLimit()){
                        // 1.4、验证这个人是否购买过。幂等性处理。如果只要秒杀成功，就去占位  userId_sessionId_skillId
                        // SETNX
                        String redisKey = repsVo.getId() + "_" + skuId;
                        // 1.4.1 自动过期--通过在redis中使用 用户id_skuId 来占位看是否买过
                        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                        if (ifAbsent){
                            // 1.4.2 占位成功，说明该用户未秒杀过该商品，则继续尝试获取库存信号量
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            boolean b = semaphore.tryAcquire(num);
                            if (b){
                                // 秒杀成功
                                // 快速下单发送MQ消息 10ms
                                String timeId = IdWorker.getTimeId();
                                SeckillOrderTo orderTo = new SeckillOrderTo();
                                orderTo.setOrderSn(timeId);
                                orderTo.setMemberId(repsVo.getId());
                                orderTo.setNum(num);
                                orderTo.setPromotionSessionId(redis.getPromotionSessionId());
                                orderTo.setSkuId(redis.getSkuId());
                                orderTo.setSeckillPrice(redis.getSeckillPrice());
                                rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderTo);
                                long s2 = System.currentTimeMillis();
                                log.info("耗时..." + (s2-s1));
                                return timeId;
                            }
                            return null;

                        }else {
                            // 说明已经买过
                            return null;
                        }

                    }

                }else {
                    return null;
                }
            }else {

                return null;
            }

        }
        return null;
    }
    // TODO 为空没有处理
    private void saveSessionInfos(List<SeckillSessionWithSkus> sessions){
        sessions.stream().forEach(session -> {
            Long startTime = session.getStartTime().getTime();
            Long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;
            Boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey){
                List<String> collect = session.getRelationEntities()
                        .stream()
                        .map(item -> item.getPromotionId().toString() +"_"+ item.getSkuId().toString())
                        .collect(Collectors.toList());
                System.out.println("saveSessionInfos------------------------"+collect);
                // 缓存活动信息
                redisTemplate.opsForList().leftPushAll(key, collect);
            }

        });

    }
    // TODO 为空没有处理
    private void saveSessionSkuInfos(List<SeckillSessionWithSkus> sessions){
        // 准备hash操作
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        sessions.stream().forEach(session -> {
            // 随机码
            String token = UUID.randomUUID().toString().replace("_", "");
            session.getRelationEntities().stream().forEach(seckillSkuVo -> {
                if (!ops.hasKey(seckillSkuVo.getPromotionSessionId().toString() + "_" + seckillSkuVo.getSkuId().toString())){
                    // 缓存商品
                    SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                    // 1、sku的基本信息
                    R r = productFeignService.getSkuInfo(seckillSkuVo.getSkuId());
                    if (0  == r.getCode()){
                        SkuInfoVo skuInfo = r.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                        });
                        redisTo.setSkuInfo(skuInfo);
                    }
                    // 2、sku的秒杀信息
                    BeanUtils.copyProperties(seckillSkuVo, redisTo);
                    // 3、设置当前商品的秒杀时间信息
                    redisTo.setStartTime(session.getStartTime().getTime());
                    redisTo.setEndTime(session.getEndTime().getTime());

                    // 4、随机码
                    redisTo.setRandomCode(token);

                    String jsonString = JSON.toJSONString(redisTo);
                    System.out.println("saveSessionSkuInfos------------------------"+jsonString);
                    ops.put(seckillSkuVo.getPromotionSessionId().toString() + "_" + seckillSkuVo.getSkuId().toString(), jsonString);
                    // 如果当前这个场次的商品的库存信息已经上架就不需要上架
                    // 5、使用库存作为分布式信号量 限流
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    // 商品可以秒杀的数量作为信号量
                    semaphore.trySetPermits(seckillSkuVo.getSeckillCount());
                }

            });
        });



    }
}
