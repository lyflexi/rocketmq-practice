package org.lyflexi.seckill_service.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.lyflexi.seckill_service.domain.Goods;

import java.util.List;

/**
 * @Author: DLJD
 * @Date:   2023/4/24
 */

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer goodsId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer goodsId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> selectSeckillGoods();

    int updateStock(Integer goodsId);
}