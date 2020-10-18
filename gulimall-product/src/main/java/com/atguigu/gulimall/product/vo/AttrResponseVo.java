package com.atguigu.gulimall.product.vo;

import lombok.Data;
import sun.plugin.dom.core.Attr;

/**
 * @author WangTianShun
 * @date 2020/10/13 13:54
 */
@Data
public class AttrResponseVo extends AttrVo {
    /**
     * "catelogName": "手机/数码/手机", //所属分类名字
     * "groupName": "主体", //所属分组名字
     */
    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
