package com.atguigu.gulimall.seckill;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1、整合sentinel
 *    1）、导入依赖 spring-cloud-starter-alibaba-sentinel
 *    2）、下载Sentinel的控制台
 *    3）、配置sentinel控制台地址信息
 *    4）、在控制台调整参数【默认所有的流控设置保存在项目内存中，重启失效】
 *
 * 2、每一个微服务导入信息审计模块spring-boot-starter-actuator
 *   并配置management.endpoints.web.exposure.include=*  （暴露Sentinel的信息）
 *
 * 3、自定义Sentinel的流控返回数据
 */

//@EnableRabbit  不用监听Rabbit,因为我们只用来发送消息，不接收消息
@EnableRedisHttpSession
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GulimallSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallSeckillApplication.class, args);
    }

}
