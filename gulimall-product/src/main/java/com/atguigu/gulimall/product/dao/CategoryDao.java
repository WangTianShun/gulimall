package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author wts
 * @email 563540326@qq.com
 * @date 2020-09-27 22:03:57
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
