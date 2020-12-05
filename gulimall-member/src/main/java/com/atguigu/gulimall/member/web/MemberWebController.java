package com.atguigu.gulimall.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: MemberWebController
 * @Author: WangTianShun
 * @Date: 2020/12/5 16:00
 * @Version 1.0
 */
@Controller
public class MemberWebController {

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(){
        //查出当前登录用户的所有订单列表数据
        return "orderList";
    }
}
