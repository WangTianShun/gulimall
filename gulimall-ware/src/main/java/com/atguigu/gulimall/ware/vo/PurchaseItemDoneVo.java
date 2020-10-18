package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * @author WangTianShun
 * @date 2020/10/17 10:39
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;

    private Integer status;

    private String reason;
}
