package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: OrderItemVo
 * @Author: WangTianShun
 * @Date: 2020/11/25 14:53
 * @Version 1.0
 */
@Data
public class OrderItemVo {
    private Long skuId;

    //标题
    private String title;

    //图片
    private String image;

    //商品套餐属性
    private List<String> skuAttr;

    //价格
    private BigDecimal price;

    //数量
    private Integer count;

    //总价
    private BigDecimal totalPrice;

    //重量
    private BigDecimal weight;
}
