package com.mall.dao;

import com.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNumberUserId(@Param("orderNumber") Long orderNumber, @Param("userId") Integer userId);

    Order selectByOrderNo(Long orderNo);
}