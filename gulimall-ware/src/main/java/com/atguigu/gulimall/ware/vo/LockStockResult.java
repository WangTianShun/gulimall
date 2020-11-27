package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * @Description: LockStockResult
 * @Author: WangTianShun
 * @Date: 2020/11/27 10:46
 * @Version 1.0
 */
@Data
public class LockStockResult {
    // 商品skuId
    private Long skuId;

    // 锁定几件
    private  Integer num;

    // 是否锁定成功
    private boolean locked;
}
