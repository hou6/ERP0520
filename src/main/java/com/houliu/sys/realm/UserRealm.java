package com.houliu.sys.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.houliu.sys.common.ActiverUser;
import com.houliu.sys.common.Constast;
import com.houliu.sys.entity.Permission;
import com.houliu.sys.entity.User;
import com.houliu.sys.service.IPermissionService;
import com.houliu.sys.service.IRoleService;
import com.houliu.sys.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author houliu
 * @create 2019-12-30 12:51
 */

public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy    //只有使用的时候才会加载
    private IUserService userService;
    @Autowired
    @Lazy
    private IPermissionService permissionService;
    @Autowired
    @Lazy
    private IRoleService roleService;

    //重写getName方法
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname",token.getPrincipal().toString());  //获取身份
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);
            //根据用户id查询percode
            //查询所有权限
            QueryWrapper<Permission> wq = new QueryWrapper<>();
            //设置只能查询菜单
            wq.eq("type", Constast.TYPE_PERMISSION);
            wq.eq("available",Constast.AVALIABLE_TRUE);
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
            List<Permission> list = new ArrayList<>();
            //通过用户的pid查询用户的权限
            if (pids.size() > 0){
                wq.in("id",pids);
                list = permissionService.list(wq);
            }
            List<String> percodes = new ArrayList<>();
            for (Permission permission : list) {
                percodes.add(permission.getPercode());
            }
            //放到activeUser
            activerUser.setPermissions(percodes);

            ByteSource byteSource = ByteSource.Util.bytes(user.getSalt());  //盐
            SimpleAuthenticationInfo authenticationInfo =
                    new SimpleAuthenticationInfo(activerUser, user.getPwd(), byteSource, this.getName());
            return authenticationInfo;
        }
        return null;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        ActiverUser activeUser = (ActiverUser) principals.getPrimaryPrincipal();
        User user = activeUser.getUser();
        List<String> permissions = activeUser.getPermissions();
        if (user.getType() == Constast.USER_TYPE_SUPER){
            authorizationInfo.addStringPermission("*:*");   //如果是超级管理员，设置所有权限
        }else {
            if (null != permissions && permissions.size() > 0){
                authorizationInfo.addStringPermissions(permissions);
            }
        }
        return authorizationInfo;
    }

}
