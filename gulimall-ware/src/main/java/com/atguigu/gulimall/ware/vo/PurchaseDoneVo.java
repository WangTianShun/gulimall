package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author WangTianShun
 * @date 2020/10/17 10:37
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;

    private List<PurchaseItemDoneVo> items;
}
