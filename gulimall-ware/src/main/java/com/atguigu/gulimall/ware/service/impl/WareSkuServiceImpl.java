package com.atguigu.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.to.mq.StockDetailTO;
import com.atguigu.common.to.mq.StockLockedTO;
import com.atguigu.common.utils.R;
import com.atguigu.common.exception.NoStockException;
import com.atguigu.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.atguigu.gulimall.ware.entity.WareOrderTaskEntity;
import com.atguigu.gulimall.ware.feign.OrderFeignService;
import com.atguigu.gulimall.ware.feign.ProductFeignService;
import com.atguigu.gulimall.ware.service.WareOrderTaskDetailService;
import com.atguigu.gulimall.ware.service.WareOrderTaskService;
import com.atguigu.gulimall.ware.vo.OrderItemVo;
import com.atguigu.gulimall.ware.vo.OrderVo;
import com.atguigu.gulimall.ware.vo.SkuHasStockVo;
import com.atguigu.gulimall.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;
import com.atguigu.gulimall.ware.dao.WareSkuDao;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.atguigu.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWapper = new QueryWrapper<>();
        String  skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)){
            queryWapper.eq("sku_id",skuId);
        }

        String  wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)){
            queryWapper.eq("ware_id",wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWapper
        );
        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断如果还没有库存记录新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (entities == null || entities.size() ==0 ){
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字  如果失败，整个事务不需要回滚

            // 1、自己catch异常
            // 2、TODO 还可以用什么办法让异常出现以后不回滚？高级
            try{
                R info = productFeignService.info(skuId);
                if (info.getCode() == 0){
                    Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }
            wareSkuDao.insert(wareSkuEntity);
        }else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }

    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            //查询sku的总库存量
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count == null ? false : count > 0);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 为某个订单锁定库存
     * (rollbackFor = NoStockException.class)
     * 默认只要都是运行异常都会回滚
     * @param vo
     *
     * 库存解锁的场景
     * 1）、下订单成功，订单过期没有支付被系统自动取消，被用户手动取消。都要解锁库存
     * 2）、下订单成功，库存锁定成功，接下来的业务调用失效，导致订单回滚。之前锁定的库存就要自动回滚
     *
     *
     * @return
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        /**
         * 保存库存工作单的详情。
         * 追溯
         */
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskService.save(taskEntity);
        // 1、按照下单的收货地址，找到一个就近仓库，锁定库存

        // 1、找到每个商品在哪个仓库都有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪个仓库有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());
        // 2、锁定库存
        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (wareIds == null || wareIds.size() == 0){
                //没有任何库存有这个商品的库存
                throw new NoStockException(skuId);
            }
            //1、如果每一个商品都锁定成功，将当前商品锁定了几件的工作单记录发给MQ
            //2、锁定失败。前面保存的工作单信息就回滚了。发送出去的消息，即使要解锁记录，由于去数据库查不到id,就不用解锁
            for (Long wareId : wareIds) {
                //成功返回1；否则就是0
                Long count = wareSkuDao.lockSkuStock(skuId,wareId,hasStock.getNum());
                if (count == 1){
                    skuStocked = true;
                    //TODO 告诉MQ库存锁定成功
                    WareOrderTaskDetailEntity detailEntity = new WareOrderTaskDetailEntity(null, skuId, "", hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    wareOrderTaskDetailService.save(detailEntity);
                    StockLockedTO stockLockedTO = new StockLockedTO();
                    StockDetailTO stockDetailTO = new StockDetailTO();
                    BeanUtils.copyProperties(detailEntity, stockDetailTO);
                    stockLockedTO.setId(taskEntity.getId());
                    //防止回滚以后找不到数据
                    stockLockedTO.setDetail(stockDetailTO);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTO);
                    break;
                } else {
                    // 当前仓库锁定库存失败 重试下一个仓库
                }
            }
            if (skuStocked == false){
                //当前商品所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }
        // 3、肯定全部都是锁定成功的
        return true;
    }

    @Override
    public void unLockStock(StockLockedTO to) {
        StockDetailTO detail = to.getDetail();
        Long skuId = detail.getSkuId();
        Long detailId = detail.getId();
        //解锁
        //1、查询数据库关于这个订单的锁定库存信息
        //有:证明库存锁定成功了
        //   解锁：订单情况
        //       1、没有这个订单。必须解锁
        //       2、有这个订单。不是解锁库存
        //               订单状态：已取消：解锁库存
        //                         没取消，不能解锁
        //没有，库存锁定失败了，库存回滚了
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(detailId);
        if (byId != null){
            //解锁
            Long id = to.getId();
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn();//根据订单号查询订单地状态
            R r = orderFeignService.getOrderStatus(orderSn);
            System.out.println("========================OrderStatus"+r.getCode());
            if (r.getCode() == 0){
                //订单数据返回成功
                OrderVo data = r.getData(new TypeReference<OrderVo>() {
                });
                if ( data == null || data.getStatus() == 4){
                    //订单不存在 或订单已经取消了。才能解锁库存
                    if (byId.getLockStatus() == 1){
                        //当前库存工作单详情，状态1 已锁定但是未解锁才可以解锁
                        unLockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                    }
                }
            }else {
                //消息拒绝以后重新放到队列里面，让别人继续消费解锁。
                throw new RuntimeException("远程调用订单服务失败");
            }
        }else {
            //无需解锁
        }
    }

    private void unLockStock(Long skuId, Long wareId, Integer num, Long taskDetailId){
        //库存解锁
        wareSkuDao.unlockStock(skuId,wareId,num,taskDetailId);
        //更新库存工作单的状态
        WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity();
        entity.setId(taskDetailId);
        entity.setLockStatus(2);//变为已解锁
        wareOrderTaskDetailService.updateById(entity);
    }


    @Data
    class SkuWareHasStock{

        private Long skuId;

        private Integer num;

        private List<Long> wareId;
    }

}