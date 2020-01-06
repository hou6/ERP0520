package com.houliu.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
import com.houliu.sys.entity.Loginfo;
import com.houliu.sys.service.ILoginfoService;
import com.houliu.sys.vo.LoginfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-23
 */
@RestController
@RequestMapping("/loginfo")
public class LoginfoController {

    @Autowired
    private ILoginfoService loginfoService;

    @RequestMapping("loadAllLoginfo")
    public DataGridView loadAllLoginfo(LoginfoVo loginfoVo){
        Page<Loginfo> page = new Page<>(loginfoVo.getPage(), loginfoVo.getLimit());
        QueryWrapper<Loginfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNoneBlank(loginfoVo.getLoginname()),"loginname",loginfoVo.getLoginname());
        queryWrapper.like(StringUtils.isNoneBlank(loginfoVo.getLoginip()),"loginip",loginfoVo.getLoginip());
        queryWrapper.ge(loginfoVo.getStartTime() != null,"logintime",loginfoVo.getStartTime());
        queryWrapper.le(loginfoVo.getEndTime() != null,"logintime",loginfoVo.getEndTime());
        queryWrapper.orderByDesc("logintime");
        this.loginfoService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("deleteLoginfo")
    public ResultObj deleteById(Integer id){
        try {
            loginfoService.removeById(id);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 根据id批量删除
     * @param loginfoVo
     * @return
     */
    @RequestMapping("/batchDeleteLoginfo")
    public ResultObj deleteByIds(LoginfoVo loginfoVo){
        try {
            List<Integer> IdList = new CopyOnWriteArrayList<>();
            for (Integer id : loginfoVo.getIds()) {
                IdList.add(id);
            }
            this.loginfoService.removeByIds(IdList);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

}

