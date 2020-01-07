package com.houliu.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.sys.common.*;
import com.houliu.sys.entity.Permission;
import com.houliu.sys.entity.User;
import com.houliu.sys.service.IPermissionService;
import com.houliu.sys.service.IRoleService;
import com.houliu.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author houliu
 * @create 2019-12-30 16:23
 *
 *      菜单管理前端控制器
 */

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IRoleService roleService;

    @RequestMapping("loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(PermissionVo permissionVo){
        //查询所有菜单
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        //设置只能查询菜单
        queryWrapper.eq("type", Constast.TYPE_MENU);
        queryWrapper.eq("available",Constast.AVALIABLE_TRUE);

        //拿出session中的user
        User user = (User) WebUtils.getSession().getAttribute("user");

        List<Permission> list = null;
        //如果是超级管理员
        if (user.getType() == Constast.USER_TYPE_SUPER) {
            list = permissionService.list(queryWrapper);
        }else {
            //根据用户ID+角色+权限去查
            Integer userId = user.getId();
            //通过用户的id查询用户的rid
            List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(userId);
            //通过用户的rids查询用户的pid
            Set<Integer> pids = new HashSet<>();     ////使用set去重
            for (Integer rid : currentUserRoleIds) {
                List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
                pids.addAll(permissionIds);
            }
            //通过用户的pid查询用户的权限
            if (pids.size() > 0){
                queryWrapper.in("id",pids);
                list = permissionService.list(queryWrapper);
            }else {
                list = new ArrayList<>();
            }

        }

        List<TreeNode> treeNodes = new ArrayList<>();
        for (Permission permission : list) {
            Integer id = permission.getId();
            Integer pid = permission.getPid();
            String title = permission.getTitle();
            String icon = permission.getIcon();
            String href = permission.getHref();
            Boolean spread = permission.getOpen() == Constast.OPEN_TRUE ? true : false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,spread));
        }
        
        //构造层级关系
        List<TreeNode> list2 = TreeNodeBuilder.build(treeNodes, 1);

        return new DataGridView(list2);

    }

    /**********************菜单管理开始************************/
    /**
     * 加载菜单左边的菜单树
     * @param permissionVo
     * @return
     */
    @RequestMapping("loadMenuManagerLeftTreeJson")
    public DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo){

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",Constast.TYPE_MENU);
        //查询出所有的菜单，存放进list中
        List<Permission> list = permissionService.list(queryWrapper);
        List<TreeNode> treeNodes = new ArrayList<>();
        //将菜单放入treeNodes中，组装成json
        for (Permission menu : list) {
            Boolean open = menu.getOpen()==1?true:false;
            treeNodes.add(new TreeNode(menu.getId(),menu.getPid(),menu.getTitle(),open));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 查询所有菜单数据
     * @param permissionVo
     * @return
     */
    @RequestMapping("loadAllMenu")
    public DataGridView loadAllMenu(PermissionVo permissionVo){
        IPage<Permission> page = new Page<>(permissionVo.getPage(),permissionVo.getLimit());
        //进行模糊查询
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(permissionVo.getId()!=null,"id",permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
        //只能查询菜单
        queryWrapper.eq("type",Constast.TYPE_MENU);
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()),"title",permissionVo.getTitle());
        queryWrapper.orderByAsc("ordernum");
        //进行查询
        permissionService.page(page,queryWrapper);
        //返回DataGridView
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("addMenu")
    public ResultObj addMenu(PermissionVo permissionVo){
        try {
            //设置添加类型为 menu
            permissionVo.setType(Constast.TYPE_MENU);
            permissionService.save(permissionVo);
            return ResultObj.ADD_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 加载排序码
     * @return
     */
    @RequestMapping("loadMenuMaxOrderNum")
    public Map<String,Object> loadMenuMaxOrderNum(){
        Map<String,Object> map = new HashMap<String,Object>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Permission> page = new Page<>(1,1);
        List<Permission> list = permissionService.page(page,queryWrapper).getRecords();
        if (list.size()>0){
            map.put("value",list.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 更新菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("updateMenu")
    public ResultObj updateMenu(PermissionVo permissionVo){
        try {
            permissionService.updateById(permissionVo);
            return ResultObj.UPDATE_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 检查当前菜单是否有子菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("checkMenuHasChildrenNode")
    public Map<String,Object> checkMenuHasChildrenNode(PermissionVo permissionVo){
        Map<String,Object> map = new HashMap<String, Object>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid",permissionVo.getId());
        List<Permission> list = permissionService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    /**
     * 删除菜单
     * @param permissionVo
     * @return
     */
    @RequestMapping("deleteMenu")
    public ResultObj deleteMenu(PermissionVo permissionVo){
        try {
            permissionService.removeById(permissionVo.getId());
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    /**********************菜单管理结束************************/

}
