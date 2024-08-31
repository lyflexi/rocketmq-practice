package org.lyflexi.seckill_service.service;


import org.lyflexi.seckill_service.domain.Order;

/**
 * @Author: DLJD
 * @Date:   2023/4/24
 */
public interface OrderService{


    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

}
