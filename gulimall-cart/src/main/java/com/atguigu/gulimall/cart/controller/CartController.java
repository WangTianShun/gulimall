package com.atguigu.gulimall.cart.controller;

import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.gulimall.cart.interceptor.CartInterceptor;
import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.Cart;
import com.atguigu.gulimall.cart.vo.CartItem;
import com.atguigu.gulimall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

/**
 * @Description: CartController
 * @Author: WangTianShun
 * @Date: 2020/11/21 14:44
 * @Version 1.0
 */
@Controller
public class CartController {

    @Autowired
    CartService cartService;
    /**
     * 浏览器有一个cookie；user-key:标识用户身份，一个月后过期
     * 如果第一次使用jd购物车功能，都会给一个临时的用户身份
     * 浏览器保存，每次访问都会带上有这个cookies
     *
     * 登录  session有
     * 没登录，按照cookie里面带来的user-key来做
     * 第一次，如果没有临时用户，帮忙创建一个临时用户
     */
    @GetMapping("/cart.html")
    public String cartListPage(Model model){

        //快速得到用户信息，id,user-key
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();


        return "cartList";
    }

    /**
     * 添加商品到购物车
     * RedirectAttributes attributes
     * attributes.addFlashAttribute();将数据放在session里面可以在页面取出，但只能取一次
     * attributes.addAttribute("skuId",skuId); 将数据放在url后面
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes attributes) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId,num);
        attributes.addAttribute("skuId",skuId);

        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    /**
     * 跳转到成功页
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,Model model){
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item",cartItem);
        return "success";
    }
}
