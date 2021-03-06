package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @Description: ItemVo
 * @Author: WangTianShun
 * @Date: 2020/11/16 16:20
 * @Version 1.0
 */
@Data
public class SkuItemVo {

    //1、sku基本信息获取    pms_sku_info
    SkuInfoEntity info;

    //2、sku的图片信息      pms_sku_images
    List<SkuImagesEntity> images;

    //3、获取spu的销售属性组合
    List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    SpuInfoDescEntity desc;

    //5、获取spu的规格参数信息
    List<SpuItemAttrGroupVo> groupAttrs;

    //6、判断有货无货
    boolean hasStock = true;

    //7、当前商品秒杀的优惠信息
    private SeckillInfoVo seckillInfo;

}
