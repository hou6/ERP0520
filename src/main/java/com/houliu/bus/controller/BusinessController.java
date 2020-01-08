package com.houliu.bus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author houliu
 * @create 2020-01-07 15:53
 *
 *      业务管理的路由器
 */

@Controller
@RequestMapping("/bus")
public class BusinessController {

    /**
     * 跳转到客户管理
     */
    @RequestMapping("toCustomerManager")
    public String toCustomerManager(){
        return "/business/customer/customerManager";
    }

    /**
     * 跳转到供应商管理
     */
    @RequestMapping("toProviderManager")
    public String toProviderManager(){
        return "/business/provider/providerManager";
    }

}
