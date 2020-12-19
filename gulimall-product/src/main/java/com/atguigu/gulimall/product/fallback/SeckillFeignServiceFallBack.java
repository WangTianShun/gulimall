package com.atguigu.gulimall.product.fallback;

import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description: SeckillFeignServiceFallBack
 * @Author: WangTianShun
 * @Date: 2020/12/18 14:30
 * @Version 1.0
 */
@Slf4j
@Component
public class SeckillFeignServiceFallBack implements SeckillFeignService {
    @Override
    public R getSkuSecKillInfo(Long skuId) {
        log.info("熔断方法调用.....getSkuSecKillInfo");
        return R.error(BizCodeEnume.TOO_MANY_REQUESTS_EXCEPTION.getCode(),BizCodeEnume.TOO_MANY_REQUESTS_EXCEPTION.getMsg());
    }
}
