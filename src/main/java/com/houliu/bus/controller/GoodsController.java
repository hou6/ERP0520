package com.houliu.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.bus.entity.Goods;
import com.houliu.bus.entity.Provider;
import com.houliu.bus.service.IGoodsService;
import com.houliu.bus.service.IProviderService;
import com.houliu.bus.vo.GoodsVo;
import com.houliu.sys.common.AppFileUtils;
import com.houliu.sys.common.Constast;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB; (`goodsid`) REFER `warehouse/bus_goods`(`id`) 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-06
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IProviderService providerService;

    /**
     * 查询
     * @param goodsVo
     * @return
     */
    @RequestMapping("loadAllGoods")
    public DataGridView loadAllGoods(GoodsVo goodsVo){
        IPage<Goods> page = new Page<>(goodsVo.getPage(),goodsVo.getLimit());
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(goodsVo.getProviderid() != null && goodsVo.getProviderid() != 0 ,"providerid",goodsVo.getProviderid());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()),"goodsname",goodsVo.getGoodsname());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()),"productcode",goodsVo.getProductcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()),"promitcode",goodsVo.getPromitcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()),"description",goodsVo.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()),"size",goodsVo.getSize());
        this.goodsService.page(page,queryWrapper);
        List<Goods> goodsList = page.getRecords();
        for (Goods goods : goodsList) {
            Provider provider = providerService.getById(goods.getProviderid());
            if (null != provider){
                goods.setProvidername(provider.getProvidername());
            }
        }
        return new DataGridView(page.getTotal(),goodsList);
    }

    /**
     * 添加
     * @param goodsVo
     * @return
     */
    @RequestMapping("addGoods")
    public ResultObj addGoods(GoodsVo goodsVo){
        try {
            if (goodsVo.getGoodsimg() != null && goodsVo.getGoodsimg().endsWith("_temp")){
                String newName = AppFileUtils.renameFile(goodsVo.getGoodsimg());
                goodsVo.setGoodsimg(newName);
            }
            this.goodsService.save(goodsVo);
            return ResultObj.ADD_OK;
        } catch (Exception e) {
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     * @param goodsVo
     * @return
     */
    @RequestMapping("updateGoods")
    public ResultObj updateGoods(GoodsVo goodsVo){
        try {
            //商品图片不是默认图片
            if (!(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().equals(Constast.DEFAULT_IMG))){
                if (goodsVo.getGoodsimg().endsWith("_temp")){
                    String newName = AppFileUtils.renameFile(goodsVo.getGoodsimg());
                    goodsVo.setGoodsimg(newName);
                    //删除原先的图片
                    String oldPath = goodsService.getById(goodsVo.getId()).getGoodsimg();
                    AppFileUtils.removeFileByPath(oldPath);
                }
            }
            this.goodsService.updateById(goodsVo);
            return ResultObj.UPDATE_OK;
        } catch (Exception e) {
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("deleteGoods")
    public ResultObj deleteGoods(Integer id,String goodsimg){
        try {
            //删除原文件
            AppFileUtils.removeFileByPath(goodsimg);
            this.goodsService.removeById(id);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

}

