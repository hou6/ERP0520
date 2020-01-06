package com.houliu.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.sys.common.Constast;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.entity.Dept;
import com.houliu.sys.entity.User;
import com.houliu.sys.service.IDeptService;
import com.houliu.sys.service.IRoleService;
import com.houliu.sys.service.IUserService;
import com.houliu.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author houliu
 * @create 2019-12-30 00:50
 */

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IRoleService roleService;

    @RequestMapping("loadAllUser")
    public DataGridView loadAllUser(UserVo userVo){
        IPage<User> page = new Page<>(userVo.getPage(),userVo.getLimit());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getName()),"loginname",userVo.getName())
                .or()
                .eq(StringUtils.isNotBlank(userVo.getName()),"name",userVo.getName());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getAddress()),"address",userVo.getAddress());
        queryWrapper.eq("type", Constast.USER_TYPE_NORMAL);
        queryWrapper.eq(userVo.getDeptid() != null,"deptid",userVo.getDeptid());
        this.userService.page(page, queryWrapper);
        List<User> userList = page.getRecords();
        for (User user : userList) {
            Integer deptid = user.getDeptid();
            if (deptid != null) {
                //先从缓存中去取，如果缓存中没有就去数据库中取
                Dept dept = deptService.getById(deptid);
                user.setDeptname(dept.getTitle());
            }
            Integer mgr = user.getMgr();
            if (mgr != null){
                User user1 = userService.getById(mgr);
                user.setLeadername(user1.getName());
            }
        }

        return new DataGridView(page.getTotal(),userList);
    }

    /**
     * 加载最大的排序码
     * @return
     */
    @RequestMapping("loadUserMaxOrderNum")
    public Map<String,Object> loadUserMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<User> page = new Page<>(1, 1);
        List<User> list = this.userService.page(page,queryWrapper).getRecords();
        if (list.size()>0) {
            map.put("value",list.get(0).getOrdernum() + 1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    @RequestMapping("loadUsersByDeptId")
    public DataGridView loadUsersByDeptId(Integer deptid){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deptid",deptid);
        queryWrapper.eq("available",Constast.AVALIABLE_TRUE);
        queryWrapper.eq("type",Constast.USER_TYPE_NORMAL);
        List<User> userList = this.userService.list(queryWrapper);
        return new DataGridView(userList);
    }

}
