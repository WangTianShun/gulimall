package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author wts
 * @email 563540326@qq.com
 * @date 2020-09-28 13:33:52
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
