package com.atguigu.gulimall.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: MyRedissonConfig
 * @Author: WangTianShun
 * @Date: 2020/11/10 13:33
 * @Version 1.0
 */
@Configuration
public class MyRedissonConfig {

    /**
     * 所有对Redisson的使用都是通过RedissonClient对象
     * @return
     */
    @Bean(destroyMethod="shutdown")
    public RedissonClient redissonClient(){
        //1、创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.20.10.9:6379");
        //2、根据config创建出RedissonClient示例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
