package com.atguigu.gulimall.member.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Description: MemberRegistVo
 * @Author: WangTianShun
 * @Date: 2020/11/18 16:48
 * @Version 1.0
 */
@Data
public class MemberRegistVo {

    private String userName;

    private String password;

    private String phone;
}
