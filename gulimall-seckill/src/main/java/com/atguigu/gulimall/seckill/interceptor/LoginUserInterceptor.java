package com.atguigu.gulimall.seckill.interceptor;

import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.vo.MemberResponseVO;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Description: LoginUserInterceptor
 * @Author: WangTianShun
 * @Date: 2020/11/25 13:42
 * @Version 1.0
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVO> loginUser = new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        boolean match = matcher.match("/kill", requestURI);
        // 如果是秒杀，需要判断是否登录，其他路径直接放行不需要判断
        if (match) {
            MemberResponseVO attribute = (MemberResponseVO) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
            if (attribute != null){
                loginUser.set(attribute);
                return true;
            }else {
                //没登录就去登录
                request.getSession().setAttribute("msg","请先进行登录");
                response.sendRedirect("http://auth.gulimall.com/login.html");
                return false;
            }
        }
        return true;
    }
}
