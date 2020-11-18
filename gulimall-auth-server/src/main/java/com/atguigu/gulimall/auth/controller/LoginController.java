package com.atguigu.gulimall.auth.controller;

import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.auth.feign.ThirdPartFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description: LoginController
 * @Author: WangTianShun
 * @Date: 2020/11/17 15:47
 * @Version 1.0
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone){
        //1、接口防刷
        String prefixPhone = AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone;
        String redisCode = stringRedisTemplate.opsForValue().get(prefixPhone);
        if (!StringUtils.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() -l < 60000){
                //60秒内不能再发
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        //如果存在的话，删除之前的验证码
        stringRedisTemplate.delete(prefixPhone);
        //2、验证码的再次校验。redis 存key-phone, value-code   sms:code:18896736055 ->12345
        String code = String.valueOf((int)((Math.random() + 1) * 100000));
        String cacheCode = code + "_" + System.currentTimeMillis();
        //redis缓存验证码   防止同一个phone在60s内再次发送验证码  set(K var1, V var2, long var3, TimeUnit var5)
        stringRedisTemplate.opsForValue().set(prefixPhone,code + "_" + System.currentTimeMillis(),10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone,code);
        return R.ok();
    }

    @PostMapping("/regist")
    public String regist(){

        //注册成功回到登录页
//        return "redirect:http://auth.gulimall.com/login.html";
        return "redirect:/login.html";
    }
}
