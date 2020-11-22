package com.atguigu.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.cart.feign.ProductFeignService;
import com.atguigu.gulimall.cart.interceptor.CartInterceptor;
import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.CartItem;
import com.atguigu.gulimall.cart.vo.SkuInfoVo;
import com.atguigu.gulimall.cart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: CartServiceImpl
 * @Author: WangTianShun
 * @Date: 2020/11/21 14:29
 * @Version 1.0
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final String CART_PREFIX = "gulimall:cart";
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        //商品信息添加到购物车
        CartItem cartItem = new CartItem();

        /**
         * 异步查询
         */
        CompletableFuture<Void> getSkuInfo = CompletableFuture.runAsync(() -> {
            //1、远程查询要添加的商品信息
            R skuInfo = productFeignService.getSkuInfo(skuId);
            SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
            });

            cartItem.setCheck(true);
            cartItem.setCount(1);
            cartItem.setImage(data.getSkuDefaultImg());
            cartItem.setTitle(data.getSkuTitle());
            cartItem.setSkuId(skuId);
            cartItem.setPrice(data.getPrice());
        },executor);
        CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
            //2、远程查询sku的组合信息
            List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
            cartItem.setSkuAttr(values);
        }, executor);
        CompletableFuture.allOf(getSkuInfo,getSkuSaleAttrValues).get();
        String jsonString = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),jsonString);

        return cartItem;
    }

    /**
     * 获取我们要操作的购物车，临时购物车、用户购物车
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        //得到用户信息 账号用户 、临时用户
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        //1、userInfoTo.getUserId()不为空表示账号用户，反之临时用户  然后决定用临时购物车还是用户购物车
        //放入缓存的key
        String cartKey = "";
        if (userInfoTo.getUserId() != null){
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        }else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }
}
