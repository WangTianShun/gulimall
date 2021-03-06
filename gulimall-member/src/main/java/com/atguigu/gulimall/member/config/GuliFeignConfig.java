package com.atguigu.gulimall.member.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 远程调用也需要用户信息，我们给他共享cookies
 * @Author: WangTianShun
 * @Date: 2020/11/26 9:10
 * @Version 1.0
 */
@Configuration
public class GuliFeignConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate requestTemplate) {
                System.out.println("RequestInterceptor线程..."+Thread.currentThread().getId());
                //1、RequestContextHolder拿到刚进来的请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null){
                    HttpServletRequest request = attributes.getRequest();//老请求
                    if (request != null){
                        //同步请求头数据。Cookie
                        String cookie = request.getHeader("Cookie");
                        //给新请求同步了老请求的cookie
                        requestTemplate.header("Cookie",cookie);
                        System.out.println("feign远程之前先执行RequestInterceptor.apply()");
                    }
                }
            }
        };
    }
}
