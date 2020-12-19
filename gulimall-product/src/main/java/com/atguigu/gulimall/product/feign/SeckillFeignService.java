package com.atguigu.gulimall.product.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: SeckillFeignService
 * @Author: WangTianShun
 * @Date: 2020/12/16 9:18
 * @Version 1.0
 */

//@FeignClient(value = "gulimall-seckill",fallback = SeckillFeignServiceFallBack.class)  //降级
@FeignClient(value = "gulimall-seckill")
public interface SeckillFeignService {

    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSecKillInfo(@PathVariable("skuId") Long skuId);
}
