package com.atguigu.common.to.mq;

import lombok.Data;

import java.util.List;

/**
 * @Description: StockLockedTO
 * @Author: WangTianShun
 * @Date: 2020/12/2 13:33
 * @Version 1.0
 */
@Data
public class StockLockedTO {

    private Long id;//库存工作单的id

    private StockDetailTO detail;//工作单详情的所有Id
}
