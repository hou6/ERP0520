package com.houliu.sys.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.sys.common.Constast;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.PinyinUtils;
import com.houliu.sys.common.ResultObj;
import com.houliu.sys.entity.Dept;
import com.houliu.sys.entity.Role;
import com.houliu.sys.entity.User;
import com.houliu.sys.service.IDeptService;
import com.houliu.sys.service.IRoleService;
import com.houliu.sys.service.IUserService;
import com.houliu.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    /**
     * 根据部门id查询用户
     * @param deptid
     * @return
     */
    @RequestMapping("loadUsersByDeptId")
    public DataGridView loadUsersByDeptId(Integer deptid){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deptid",deptid);
        queryWrapper.eq("available",Constast.AVALIABLE_TRUE);
        queryWrapper.eq("type",Constast.USER_TYPE_NORMAL);
        List<User> userList = this.userService.list(queryWrapper);
        return new DataGridView(userList);
    }

    /**
     * 把用户输入的汉字转为拼音
     * @param username
     * @return
     */
    @RequestMapping("changeChineseToPinyin")
    public Map<String,Object> changeChineseToPinyin(String username){
        Map<String,Object> map = new HashMap<>();
        if (null != username){
            map.put("value", PinyinUtils.getPingYin(username));
        }else {
            map.put("value","");
        }
        return map;
    }

    /**
     * 添加用户
     * @param userVo
     * @return
     */
    @RequestMapping("addUser")
    public ResultObj addUser(UserVo userVo){
        try {
            userVo.setType(Constast.USER_TYPE_NORMAL);  //设置用户类型
            userVo.setHiredate(new Date());
            String salt = IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);   //设置盐
            userVo.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());  //设置密码
            this.userService.save(userVo);
            return ResultObj.ADD_OK;
        }catch (Exception e){
            return ResultObj.ADD_ERROR;
        }
    }

    @RequestMapping("loadUserById")
    public DataGridView loadUserById(Integer id){
        return new DataGridView(this.userService.getById(id));
    }

    /**
     * 更新用户
     * @param userVo
     * @return
     */
    @RequestMapping("updateUser")
    public ResultObj updateUser(UserVo userVo){
        try {
            this.userService.updateById(userVo);
            return ResultObj.UPDATE_OK;
        }catch (Exception e){
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("deleteUser")
    public ResultObj deleteUser(Integer id){
        try {
            this.userService.removeById(id);
            return ResultObj.DELETE_OK;
        }catch (Exception e){
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @RequestMapping("resetPwd")
    public ResultObj resetPwd(Integer id){
        try {
            User user = new User();
            user.setId(id);
            String salt = IdUtil.simpleUUID().toUpperCase();
            user.setSalt(salt);
            user.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());
            this.userService.updateById(user);
            return ResultObj.RESET_OK;
        }catch (Exception e){
            return ResultObj.RESET_ERROR;
        }
    }

    @RequestMapping("initRoleByUserId")
    public DataGridView initRoleByUserId(Integer id){
        //查询所有可用的角色
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available",Constast.AVALIABLE_TRUE);
        List<Map<String, Object>> listMaps = this.roleService.listMaps(queryWrapper);
//        listMaps.forEach(System.out::println);
        //查询当前用户所拥有的角色id
        List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(id);
        for (Map<String, Object> map : listMaps) {
            Boolean LAY_CHECKED = false;
            Integer roleId = (Integer) map.get("id");
            for (Integer rid : currentUserRoleIds) {
                if (rid == roleId){
                    LAY_CHECKED = true;
                    break;
                }
            }
            map.put("LAY_CHECKED",LAY_CHECKED);
        }
        return new DataGridView(Long.valueOf(listMaps.size()),listMaps);

    }

    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param ids
     * @return
     */
    @RequestMapping("saveUserRole")
    public ResultObj saveUserRole(Integer uid,Integer[] ids){
        try {
            userService.saveUserRole(uid,ids);
            return ResultObj.ADD_OK;
        }catch (Exception e){
            return ResultObj.ADD_ERROR;
        }

    }


}
