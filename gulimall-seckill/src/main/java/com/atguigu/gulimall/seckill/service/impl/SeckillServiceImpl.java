package com.atguigu.gulimall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.seckill.fegin.CouponFeignService;
import com.atguigu.gulimall.seckill.fegin.ProductFeignService;
import com.atguigu.gulimall.seckill.service.SeckillService;
import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;
import com.atguigu.gulimall.seckill.vo.SeckillSessionWithSkus;
import com.atguigu.gulimall.seckill.vo.SkuInfoVo;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Description: SeckillServiceImpl
 * @Author: WangTianShun
 * @Date: 2020/12/13 9:51
 * @Version 1.0
 */
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

            // 1、缓存活动信息
            saveSessionInfos(data);

            // 2、缓存获得关联商品信息
            saveSessionSkuInfos(data);
        }
    }

    private void saveSessionInfos(List<SeckillSessionWithSkus> sessions){
        sessions.stream().forEach(session -> {
            Long startTime = session.getStartTime().getTime();
            Long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;
            List<String> collect = session.getRelationEntities().stream().map(item -> item.getSkuId().toString()).collect(Collectors.toList());
            // 缓存活动信息
            redisTemplate.opsForList().leftPushAll(key, collect);
        });
    }

    private void saveSessionSkuInfos(List<SeckillSessionWithSkus> sessions){
        // 准备hash操作
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        sessions.stream().forEach(session -> {
            session.getRelationEntities().stream().forEach(seckillSkuVo -> {
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
                String token = UUID.randomUUID().toString().replace("_", "");
                redisTo.setRandomCode(token);
                // 5、使用库存作为分布式信号量 限流
                RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                // 商品可以秒杀的数量作为信号量
                semaphore.trySetPermits(seckillSkuVo.getSeckillCount());

                String jsonString = JSON.toJSONString(redisTo);
                ops.put(seckillSkuVo.getSkuId(), jsonString);
            });
        });

    }
}
