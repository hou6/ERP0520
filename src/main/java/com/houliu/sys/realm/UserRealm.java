package com.houliu.sys.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.houliu.sys.common.ActiverUser;
import com.houliu.sys.entity.User;
import com.houliu.sys.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * @author houliu
 * @create 2019-12-30 12:51
 */

public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy    //只有使用的时候才会加载
    private IUserService userService;

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

        return null;
    }

}
