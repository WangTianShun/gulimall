package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author wts
 * @email 563540326@qq.com
 * @date 2020-09-28 10:48:33
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
