package com.atguigu.common.exception;

/**
 * @Description: NoStockException
 * @Author: WangTianShun
 * @Date: 2020/11/27 11:49
 * @Version 1.0
 */
public class NoStockException extends RuntimeException {

    private Long skuId;

    public NoStockException(String msg) {
        super(msg);
    }
    public NoStockException(Long skuId){
        super("商品id:" + skuId + "没有足够的库存了");
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSkuId() {
        return skuId;
    }
}
