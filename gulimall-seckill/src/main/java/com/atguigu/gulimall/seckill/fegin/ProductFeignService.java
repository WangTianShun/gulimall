package com.atguigu.gulimall.seckill.fegin;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @Description: ProductFeignService
 * @Author: WangTianShun
 * @Date: 2020/12/13 11:25
 * @Version 1.0
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);
}
