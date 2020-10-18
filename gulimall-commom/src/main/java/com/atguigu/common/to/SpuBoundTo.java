package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author WangTianShun
 * @date 2020/10/16 9:20
 */
@Data
public class SpuBoundTo {
    private Long SpuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
