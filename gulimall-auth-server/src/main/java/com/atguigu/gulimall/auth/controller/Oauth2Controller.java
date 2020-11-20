package com.atguigu.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.HttpUtils;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.common.vo.MemberResponseVO;
import com.atguigu.gulimall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 处理社交登录请求
 * @Author: WangTianShun
 * @Date: 2020/11/19 14:47
 * @Version 1.0
 */
@Slf4j
@Controller
public class Oauth2Controller {

    @Autowired
    MemberFeignService memberFeignService;

    /**
     * 社交登录回调
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session, RedirectAttributes attributes) throws Exception {
        //1. 使用code换取token，换取成功则继续2，否则重定向至登录页
        Map<String,String> map = new HashMap<>();
        map.put("client_id","798445888");
        map.put("client_secret","7886f4db232d2e932690e08e346c3e67");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("code",code);
        //1、根据code换取accessToken
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<>(), map, new HashMap<>());
        Map<String, String> errors = new HashMap<>();
        if (response.getStatusLine().getStatusCode() == 200){
            //2. 调用member远程接口进行oauth登录，登录成功则转发至首页并携带返回用户信息，否则转发至登录页
            String jsonString = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(jsonString, SocialUser.class);
            //1)、当前用户如果是第一次进网站，自动注册进来（为当前社交用户生成一个会员信息账号，以后这个社交账号就对应指定的会员）
            // 获取用户的登录平台，然后判断用户是否该注册到系统中
            R r = memberFeignService.oauth2Login(socialUser);
            //2.1 远程调用成功，返回首页并携带用户信息
            if (r.getCode() == 0) {
                // session 子域共享问题
                MemberResponseVO loginUser = r.getData(new TypeReference<MemberResponseVO>() {});
                log.info("登陆成功：用户信息"+loginUser.toString());
                //TODO 1、默认发的令牌。 session=dakadja; 作用域：当前域。（解决子域session共享问题）
                //TODO 2、使用json的序列化方式来序列化对象数据到redis中
                session.setAttribute("loginUser", loginUser);

                return "redirect:http://gulimall.com";
            } else {
                //2.2 否则返回登录页
                errors.put("msg", "登录失败，请重试");
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.catmall.com/login.html ";
            }
        }else {
            errors.put("msg", "获得第三方授权失败，请重试");
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }
}
