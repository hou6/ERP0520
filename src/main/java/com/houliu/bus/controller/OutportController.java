package com.houliu.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.bus.entity.Goods;
import com.houliu.bus.entity.Outport;
import com.houliu.bus.entity.Provider;
import com.houliu.bus.service.IGoodsService;
import com.houliu.bus.service.IOutportService;
import com.houliu.bus.service.IProviderService;
import com.houliu.bus.vo.OutportVo;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/outport")
public class OutportController {

    @Autowired
    private IOutportService outportService;
    @Autowired
    private IProviderService providerService;
    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询
     * @param outportVo
     * @return
     */
    @RequestMapping("loadAllOutport")
    public DataGridView loadAllOutport(OutportVo outportVo){

        IPage<Outport> page = new Page<>(outportVo.getPage(),outportVo.getLimit());
        QueryWrapper<Outport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(outportVo.getProviderid() != null && outportVo.getProviderid() != 0 ,"providerid",outportVo.getProviderid());
        queryWrapper.eq(outportVo.getGoodsid() != null && outportVo.getGoodsid() != 0 ,"goodsid",outportVo.getGoodsid());
        queryWrapper.ge(outportVo.getStartTime() != null ,"outporttime",outportVo.getStartTime());
        queryWrapper.le(outportVo.getEndTime() != null ,"outporttime",outportVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getOperateperson()),"operateperson",outportVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getRemark()),"remark",outportVo.getRemark());
        queryWrapper.orderByDesc("outporttime");
        this.outportService.page(page,queryWrapper);
        List<Outport> outportList = page.getRecords();
        for (Outport outport : outportList) {
            Provider provider = providerService.getById(outport.getProviderid());
            if (null != provider){
                outport.setProvidername(provider.getProvidername());
            }
            Goods goods = goodsService.getById(outport.getGoodsid());
            if (null != goods) {
                outport.setGoodsname(goods.getGoodsname());
                outport.setSize(goods.getSize());
            }
        }
        return new DataGridView(page.getTotal(),outportList);
    }


    /**
     * 添加退货信息
     * @param id
     * @param number
     * @param remark
     * @return
     */
    @RequestMapping("addOutport")
    public ResultObj addOutport(Integer id,Integer number,String remark){
        try {
            this.outportService.addOutPort(id,number,remark);
            return ResultObj.OPERATE_OK;
        }catch (Exception e){
            return ResultObj.OPERATE_ERROR;
        }

    }

}

