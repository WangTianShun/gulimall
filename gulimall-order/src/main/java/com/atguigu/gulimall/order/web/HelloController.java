package com.atguigu.gulimall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: HelloController
 * @Author: WangTianShun
 * @Date: 2020/11/25 9:30
 * @Version 1.0
 */
@Controller
public class HelloController {

    @GetMapping("{page}.html")
    public String listPage(@PathVariable("page") String page){

        return page;
    }
}
