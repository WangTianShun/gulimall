package com.atguigu.gulimall.order.vo;

import lombok.Data;

/**
 * @Description: SkuStockVo
 * @Author: WangTianShun
 * @Date: 2020/11/26 14:14
 * @Version 1.0
 */
@Data
public class SkuStockVo {
    private Long skuId;
    private Boolean hasStock;
}
