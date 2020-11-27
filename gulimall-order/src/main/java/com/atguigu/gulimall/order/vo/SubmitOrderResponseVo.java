package com.atguigu.gulimall.order.vo;


import com.atguigu.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @Description: SubmitOrderResponseVo
 * @Author: WangTianShun
 * @Date: 2020/11/26 22:27
 * @Version 1.0
 */
@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    private Integer code;

}
