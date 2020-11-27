package com.atguigu.gulimall.order.vo;

import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.util.List;

/**
 * @Description: WareSkuLockVo
 * @Author: WangTianShun
 * @Date: 2020/11/27 10:41
 * @Version 1.0
 */
@Data
public class WareSkuLockVo {

    // 订单号
    private String orderSn;

    // 需要锁住的所有库存信息
    private List<OrderItemVo> locks;
}
