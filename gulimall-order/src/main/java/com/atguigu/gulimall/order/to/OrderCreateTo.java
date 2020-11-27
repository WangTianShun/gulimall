package com.atguigu.gulimall.order.to;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: OrderCreateTo
 * @Author: WangTianShun
 * @Date: 2020/11/27 9:06
 * @Version 1.0
 */
@Data
public class OrderCreateTo {

    // 订单
    private OrderEntity order;

    // 订单项
    private List<OrderItemEntity> orderItems;

    // 订单应付的价格
    private BigDecimal payPrice;

    //运费
    private BigDecimal fare;
}
