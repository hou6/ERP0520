package com.houliu.sys.common;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author houliu
 * @create 2019-12-30 14:49
 */
public class WebUtils {

    /**
     * 得到requst
     * @return
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request;
    }

    /**
     * 得到sesssion
     */
    public static HttpSession getSession(){
        return getRequest().getSession();
    }

}
