package com.atguigu.gulimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberResponseVO;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.auth.feign.ThirdPartFeignService;
import com.atguigu.gulimall.auth.vo.UserLoginVo;
import com.atguigu.gulimall.auth.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    MemberFeignService memberFeignService;

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

        //2、验证码的再次校验。redis 存key-phone, value-code   sms:code:18896736055 ->12345
//        String code = String.valueOf((int)((Math.random() + 1) * 100000));
        String code = "123456";
        //redis缓存验证码   防止同一个phone在60s内再次发送验证码  set(K var1, V var2, long var3, TimeUnit var5)
        stringRedisTemplate.opsForValue().set(prefixPhone,code + "_" + System.currentTimeMillis(),10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone,code);
        return R.ok();
    }

    /**
     * //TODO 重定向携带数据，利用session原理。将数据放在session中。只要跳到下一个页面，取出数据以后，session里面的数据就会删掉
     * //TODO  1、分布式下的session问题
     * RedirectAttributes redirectAttributes 模拟重定向携带数据
     * @param vo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result,
                         RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            /**
             * 方法一
             * Map<String, String> errors = result.getFieldErrors().stream().map(fieldError ->{
             *                 String field = fieldError.getField();
             *                 String defaultMessage = fieldError.getDefaultMessage();
             *                 errors.put(field,defaultMessage);
             *                 return errors;
             *             }).collect(Collector.asList());
             */
            //方法二：
            //1.1 如果校验不通过，则封装校验结果
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            //1.2 将错误信息封装到session中
            redirectAttributes.addFlashAttribute("errors",errors);
            /**
             * 使用 return "forward:/reg.html"; 会出现
             * 问题：Request method 'POST' not supported的问题
             * 原因：用户注册-> /regist[post] ------>转发/reg.html (路径映射默认都是get方式访问的)
             * 校验出错转发到注册页
             */
            //return "reg";    //转发会出现重复提交的问题，不要以转发的方式
            //使用重定向  解决重复提交的问题。但面临着数据不能携带的问题，就用RedirectAttributes
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //1、校验验证码
        String code = vo.getCode();
        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (!StringUtils.isEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                //验证码通过,删除缓存中的验证码；令牌机制
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //真正注册调用远程服务注册
                R r = memberFeignService.regist(vo);
                if (r.getCode() == 0) {
                    //成功
                    return "redirect:http://auth.gulimall.com/login.html";
                } else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData(new TypeReference<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", errors);
                }
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            //校验出错转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //注册成功回到登录页
        //return "redirect:http://auth.gulimall.com/login.html";
        return "redirect:http://auth.gulimall.com/login.html";
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute == null) {
            //没登录
            return "login";
        } else{
            return "redirect:http://gulimall.com";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes, HttpSession session){
        //远程登录
        R r = memberFeignService.login(vo);
        if (r.getCode() == 0){
            MemberResponseVO loginUser = r.getData(new TypeReference<MemberResponseVO>() {});
            //成功放到session中
            session.setAttribute(AuthServerConstant.LOGIN_USER,loginUser);
            return "redirect:http://gulimall.com";
        }else{
            Map<String,String> errors = new HashMap<>();
            errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}
