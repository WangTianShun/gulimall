package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author WangTianShun
 * @date 2020/10/16 22:17
 */
@Data
public class MergeVo {

    private Long purchaseId; //整单id
    private List<Long> items;//[1,2,3,4] //合并项集合
}
