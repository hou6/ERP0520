package com.houliu.sys.controller;

import com.houliu.sys.common.ActiverUser;
import com.houliu.sys.common.ResultObj;
import com.houliu.sys.common.WebUtils;
import com.houliu.sys.entity.Loginfo;
import com.houliu.sys.service.ILoginfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author houliu
 * @create 2019-12-30 14:29
 */

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private ILoginfoService loginfoService;

    @RequestMapping("login")
    public ResultObj login(String loginname,String pwd){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd);
        try {
            subject.login(token);
            //登录成功，把用户信息存入session
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            WebUtils.getSession().setAttribute("user",activerUser.getUser());

            //记录登录日志
            Loginfo entity = new Loginfo();
            entity.setLoginname(activerUser.getUser().getName() + "-" + activerUser.getUser().getLoginname());
            entity.setLoginip(WebUtils.getRequest().getRemoteAddr());
            entity.setLogintime(new Date());
            loginfoService.save(entity);

            return ResultObj.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            return ResultObj.LOGIN_ERROR_PASS;
        }
    }

}
