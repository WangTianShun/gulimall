package com.atguigu.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: ThreadPoolConfigProperties
 * @Author: WangTianShun
 * @Date: 2020/11/17 13:40
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "gulimall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    private Integer core;
    private Integer maxSize;
    private Integer keepAliveTime;
}
