package org.lyflexi.seckill_service.service.impl;

import jakarta.annotation.Resource;
import org.lyflexi.seckill_service.domain.Order;
import org.lyflexi.seckill_service.mapper.OrderMapper;
import org.lyflexi.seckill_service.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @Author: DLJD
 * @Date:   2023/4/24
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Order record) {
        return orderMapper.insert(record);
    }

    @Override
    public int insertSelective(Order record) {
        return orderMapper.insertSelective(record);
    }

    @Override
    public Order selectByPrimaryKey(Integer id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Order record) {
        return orderMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Order record) {
        return orderMapper.updateByPrimaryKey(record);
    }

}
