package com.atguigu.gulimall.seckill.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: Sentinel-自定义流控响应
 * @Author: WangTianShun
 * @Date: 2020/12/17 10:55
 * @Version 1.0
 */
@Configuration
public class SeckillSentinelConfig implements BlockExceptionHandler {
    /**
     * 2.2.0以后的版本实现的是BlockExceptionHandler；以前的版本实现的是WebCallbackManager
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws Exception
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        R error = R.error(BizCodeEnume.TOO_MANY_REQUESTS_EXCEPTION.getCode(), BizCodeEnume.TOO_MANY_REQUESTS_EXCEPTION.getMsg());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(JSON.toJSONString(error));
    }

    /**
     * 因为版本冲突导致无法引入 WebCallbackManager
     */
//    public SeckillSentinelConfig() {
//        WebCallbackManager.setUrlBlockHandler((request, response, ex) -> {
//            R error = R.error(BizCodeEnum.TOO_MANY_REQUESTS_EXCEPTION.getCode(), BizCodeEnum.TOO_MANY_REQUESTS_EXCEPTION.getMsg());
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json");
//            response.getWriter().write(JSON.toJSONString(error));
//        });
//    }
}
