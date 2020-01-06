package com.houliu.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
import com.houliu.sys.common.TreeNode;
import com.houliu.sys.entity.Dept;
import com.houliu.sys.service.IDeptService;
import com.houliu.sys.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-26
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private IDeptService deptService;

    /**
     * 加载部门管理左边的部门树的json
     * @param deptVo
     * @return
     */
    @RequestMapping("loadDeptManagerLeftTreeJson")
    public DataGridView loadDeptManagerLeftTreeJson(DeptVo deptVo){
        List<Dept> deptList = this.deptService.list();
        List<TreeNode> treeNodes = new CopyOnWriteArrayList<>();
        for (Dept dept : deptList) {
            Boolean spread = dept.getOpen() == 1 ? true : false;
            treeNodes.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),spread));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 查询
     * @param deptVo
     * @return
     */
    @RequestMapping("loadAllDept")
    public DataGridView loadAllDept(DeptVo deptVo){
        IPage<Dept> page = new Page<>(deptVo.getPage(),deptVo.getLimit());
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()),"title",deptVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()),"address",deptVo.getAddress());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getRemark()),"remark",deptVo.getRemark());
        queryWrapper.eq(deptVo.getId() != null,"id",deptVo.getId()).or().eq(deptVo.getId() != null,"pid",deptVo.getId());
        queryWrapper.orderByDesc("orderNum");
        this.deptService.page(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加部门
     * @param deptVo
     * @return
     */
    @RequestMapping("addDept")
    public ResultObj addDept(DeptVo deptVo){
        try {
            deptVo.setCreatetime(new Date());
            this.deptService.save(deptVo);
            return ResultObj.ADD_OK;
        } catch (Exception e) {
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 更新
     * @param deptVo
     * @return
     */
    @RequestMapping("updateDept")
    public ResultObj updateDept(DeptVo deptVo){
        try {
            this.deptService.updateById(deptVo);
            return ResultObj.UPDATE_OK;
        } catch (Exception e) {
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除
     * @param deptVo
     * @return
     */
    @RequestMapping("deleteDept")
    public ResultObj deleteDept(DeptVo deptVo){
        try {
            this.deptService.removeById(deptVo.getId());
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 检查当前部门是否有子部门
     * @param deptVo
     * @return
     */
    @RequestMapping("checkDeptHasChildrenNode")
    public Map<String,Object> checkDeptHasChildrenNode(DeptVo deptVo){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid",deptVo.getId());
        List<Dept> list = this.deptService.list(queryWrapper);
        if (list.size() > 0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    /**
     * 加载最大的排序码
     * @return
     */
    @RequestMapping("loadDeptMaxOrderNum")
    public Map<String,Object> loadDeptMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        List<Dept> list = this.deptService.list(queryWrapper);
        if (list.size()>0) {
            map.put("value",list.get(0).getOrdernum() + 1);
        }else {
            map.put("value",1);
        }
        return map;
    }




}

