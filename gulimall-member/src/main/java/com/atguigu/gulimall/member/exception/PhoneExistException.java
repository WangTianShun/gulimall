package com.atguigu.gulimall.member.exception;

/**
 * @Description: PhoneExistException
 * @Author: WangTianShun
 * @Date: 2020/11/18 20:55
 * @Version 1.0
 */
public class PhoneExistException extends RuntimeException {
    public PhoneExistException(){
        super("手机号存在");
    }
}
