package com.houliu.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houliu.bus.entity.Goods;
import com.houliu.bus.entity.Inport;
import com.houliu.bus.entity.Outport;
import com.houliu.bus.mapper.GoodsMapper;
import com.houliu.bus.mapper.InportMapper;
import com.houliu.bus.mapper.OutportMapper;
import com.houliu.bus.service.IOutportService;
import com.houliu.sys.common.WebUtils;
import com.houliu.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * InnoDB free: 9216 kB 服务实现类
 * </p>
 *
 * @author houliu
 * @since 2019-12-19
 */
@Service
@Transactional
public class OutportServiceImpl extends ServiceImpl<OutportMapper, Outport> implements IOutportService {

    @Autowired
    private InportMapper inportMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void addOutPort(Integer id, Integer number, String remark) {
        Inport inport = inportMapper.selectById(id);
        Goods goods = goodsMapper.selectById(inport.getGoodsid());
        goods.setNumber(goods.getNumber() - number);
        goodsMapper.updateById(goods);

        Outport entity = new Outport();
        entity.setNumber(number);
        entity.setGoodsid(inport.getGoodsid());
        User user = (User) WebUtils.getSession().getAttribute("user");
        entity.setOperateperson(user.getName());
        entity.setOutporttime(new Date());
        entity.setPaytype(inport.getPaytype());
        entity.setOutportprice(inport.getInportprice());
        entity.setProviderid(inport.getProviderid());
        entity.setRemark(remark);
        this.getBaseMapper().insert(entity);
    }
}
