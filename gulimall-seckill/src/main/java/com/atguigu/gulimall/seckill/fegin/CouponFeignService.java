package com.atguigu.gulimall.seckill.fegin;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: CouponFeignService
 * @Author: WangTianShun
 * @Date: 2020/12/13 18:26
 * @Version 1.0
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/lasts3DaySession")
    R getLasts3DaySession();
}

