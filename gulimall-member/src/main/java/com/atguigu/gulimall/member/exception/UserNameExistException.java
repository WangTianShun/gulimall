package com.atguigu.gulimall.member.exception;

/**
 * @Description: UserNameExistException
 * @Author: WangTianShun
 * @Date: 2020/11/18 20:54
 * @Version 1.0
 */
public class UserNameExistException extends RuntimeException {
    public UserNameExistException(){
        super("用户名已存在");
    }
}
