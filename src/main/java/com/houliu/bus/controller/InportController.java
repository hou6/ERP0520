package com.houliu.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.bus.entity.Goods;
import com.houliu.bus.entity.Inport;
import com.houliu.bus.entity.Provider;
import com.houliu.bus.service.IGoodsService;
import com.houliu.bus.service.IInportService;
import com.houliu.bus.service.IProviderService;
import com.houliu.bus.vo.InportVo;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
import com.houliu.sys.common.WebUtils;
import com.houliu.sys.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB; (`providerid`) REFER `warehouse/bus_provider`(`id`); (`goo 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-18
 */
@RestController
@RequestMapping("inport")
public class InportController {

    @Autowired
    private IInportService inportService;
    @Autowired
    private IProviderService providerService;
    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询
     * @param inportVo
     * @return
     */
    @RequestMapping("loadAllInport")
    public DataGridView loadAllInport(InportVo inportVo){

        IPage<Inport> page = new Page<>(inportVo.getPage(),inportVo.getLimit());
        QueryWrapper<Inport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(inportVo.getProviderid() != null && inportVo.getProviderid() != 0 ,"providerid",inportVo.getProviderid());
        queryWrapper.eq(inportVo.getGoodsid() != null && inportVo.getGoodsid() != 0 ,"goodsid",inportVo.getGoodsid());
        queryWrapper.ge(inportVo.getStartTime() != null ,"inporttime",inportVo.getStartTime());
        queryWrapper.le(inportVo.getEndTime() != null ,"inporttime",inportVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(inportVo.getOperateperson()),"operateperson",inportVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(inportVo.getRemark()),"remark",inportVo.getRemark());
        queryWrapper.orderByDesc("inporttime");
        this.inportService.page(page,queryWrapper);
        List<Inport> inportList = page.getRecords();
        for (Inport inport : inportList) {
            Provider provider = providerService.getById(inport.getProviderid());
            if (null != provider){
                inport.setProvidername(provider.getProvidername());
            }
            Goods goods = goodsService.getById(inport.getGoodsid());
            if (null != goods) {
                inport.setGoodsname(goods.getGoodsname());
                inport.setSize(goods.getSize());
            }
        }
        return new DataGridView(page.getTotal(),inportList);
    }

    /**
     * 添加
     * @param inportVo
     * @return
     */
    @RequestMapping("addInport")
    public ResultObj addInport(InportVo inportVo){
        try {
            inportVo.setInporttime(new Date());
            //获得当前系统用户
            User user = (User) WebUtils.getSession().getAttribute("user");
            //设置操作人
            inportVo.setOperateperson(user.getName());
            //设置进货时间
            inportService.save(inportVo);
            return ResultObj.ADD_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 更新进货商品
     * @param inportVo
     * @return
     */
    @RequestMapping("updateInport")
    public ResultObj updateInport(InportVo inportVo){
        try {
            inportService.updateById(inportVo);
            return ResultObj.UPDATE_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }

    }

    /**
     * 删除进货商品
     * @param id
     * @return
     */
    @RequestMapping("deleteInport")
    public ResultObj deleteInport(Integer id){
        try {
            inportService.removeById(id);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }

    }

}

