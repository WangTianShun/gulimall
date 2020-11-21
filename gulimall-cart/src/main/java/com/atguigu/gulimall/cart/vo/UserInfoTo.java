package com.atguigu.gulimall.cart.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description: UserInfoVo
 * @Author: WangTianShun
 * @Date: 2020/11/21 15:03
 * @Version 1.0
 */
@ToString
@Data
public class UserInfoTo {

    private Long userId;

    private String userKey; //一定封装

    private boolean tempUser = false;  //判断是否有临时用户
}
