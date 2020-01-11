package com.houliu.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houliu.bus.entity.Goods;
import com.houliu.bus.entity.Inport;
import com.houliu.bus.mapper.GoodsMapper;
import com.houliu.bus.mapper.InportMapper;
import com.houliu.bus.service.IInportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 9216 kB; (`providerid`) REFER `warehouse/bus_provider`(`id`); (`goo 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-18
 */
@Service
@Transactional
public class InportServiceImpl extends ServiceImpl<InportMapper, Inport> implements IInportService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public boolean save(Inport entity) {
        //根据商品编号查到商品
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        goods.setNumber(goods.getNumber() + entity.getNumber());
        goodsMapper.updateById(goods);
        //保存进货信息
        return super.save(entity);
    }

    @Override
    public boolean updateById(Inport entity) {
        //根据进货ID查询进货
        Inport inport = this.baseMapper.selectById(entity.getId());
        //根据商品ID查询商品
        Goods goods = this.goodsMapper.selectById(entity.getGoodsid());
        //设置数量=当前数量-进货单修改之前的数量+修改的数量
        goods.setNumber(goods.getNumber() - inport.getNumber() + entity.getNumber());
        this.goodsMapper.updateById(goods);
        //更新进货单
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        //根据进货ID查询进货
        Inport inport = this.baseMapper.selectById(id);
        //根据商品ID查询商品
        Goods goods = this.goodsMapper.selectById(inport.getGoodsid());
        //设置数量=当前数量-进货单修改之前的数量+修改的数量
        goods.setNumber(goods.getNumber() - inport.getNumber());
        this.goodsMapper.updateById(goods);

        return super.removeById(id);
    }
}
