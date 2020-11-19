package com.atguigu.gulimall.member.vo;

import lombok.Data;

/**
 * @Description: SocialUser
 * @Author: WangTianShun
 * @Date: 2020/11/19 15:50
 * @Version 1.0
 */
@Data
public class SocialUser {
    private String access_token;
    private String remind_in;
    private long expires_in;
    private String uid;
    private String isRealName;
}
