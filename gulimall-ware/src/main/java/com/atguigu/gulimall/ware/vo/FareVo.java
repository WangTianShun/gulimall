package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: FareVo
 * @Author: WangTianShun
 * @Date: 2020/11/26 20:58
 * @Version 1.0
 */

@Data
public class FareVo {
    //收货人地址信息
    private MemberAddressVo address;
    //费用
    private BigDecimal fare;
}
