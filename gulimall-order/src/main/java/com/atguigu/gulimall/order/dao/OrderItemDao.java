package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author wts
 * @email 563540326@qq.com
 * @date 2020-09-28 10:48:33
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
