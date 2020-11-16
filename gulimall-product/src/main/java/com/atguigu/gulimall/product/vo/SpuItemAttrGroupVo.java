package com.atguigu.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: SpuItemAttrGroupVo
 * @Author: WangTianShun
 * @Date: 2020/11/16 21:02
 * @Version 1.0
 */
@ToString
@Data
public class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;
}
