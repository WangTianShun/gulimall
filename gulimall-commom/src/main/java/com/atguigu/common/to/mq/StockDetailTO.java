package com.atguigu.common.to.mq;

import lombok.Data;

/**
 * @Description: StockDetailTO
 * @Author: WangTianShun
 * @Date: 2020/12/2 13:43
 * @Version 1.0
 */
@Data
public class StockDetailTO {
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;
    /**
     * 仓库id
     */
    private long wareId;
    /**
     * 锁定状态
     */
    private Integer lockStatus;
}
