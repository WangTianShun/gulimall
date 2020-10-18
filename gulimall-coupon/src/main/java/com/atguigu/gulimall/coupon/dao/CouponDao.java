package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author wts
 * @email 563540326@qq.com
 * @date 2020-09-28 09:53:40
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
