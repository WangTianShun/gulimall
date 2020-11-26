package com.atguigu.gulimall.order.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: WmsFeignService
 * @Author: WangTianShun
 * @Date: 2020/11/26 14:11
 * @Version 1.0
 */
@FeignClient("gulimall-ware")
public interface WmsFeignService {

    //查询sku是否有库存
    @PostMapping("/ware/waresku/hasStock")
    public R getSkusHasStock(@RequestBody List<Long> skuIds);
}
