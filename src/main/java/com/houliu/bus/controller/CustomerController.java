package com.houliu.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.bus.entity.Customer;
import com.houliu.bus.service.ICustomerService;
import com.houliu.bus.vo.CustomerVo;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
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
 * @since 2019-12-05
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    /**
     * 查询
     * @param customerVo
     * @return
     */
    @RequestMapping("loadAllCustomer")
    public DataGridView loadAllCustomer(CustomerVo customerVo){
        IPage<Customer> page = new Page<>(customerVo.getPage(),customerVo.getLimit());
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()),"customername",customerVo.getCustomername());
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getPhone()),"phone",customerVo.getPhone());
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getConnectionpersion()),"connectionpersion",customerVo.getConnectionpersion());
        this.customerService.page(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加
     * @param customerVo
     * @return
     */
    @RequestMapping("addCustomer")
    public ResultObj addCustomer(CustomerVo customerVo){
        try {
            this.customerService.save(customerVo);
            return ResultObj.ADD_OK;
        } catch (Exception e) {
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     * @param customerVo
     * @return
     */
    @RequestMapping("updateCustomer")
    public ResultObj updateCustomer(CustomerVo customerVo){
        try {
            this.customerService.updateById(customerVo);
            return ResultObj.UPDATE_OK;
        } catch (Exception e) {
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("deleteCustomer")
    public ResultObj deleteCustomer(Integer id){
        try {
            this.customerService.removeById(id);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 根据id批量删除
     * @param customerVo
     * @return
     */
    @RequestMapping("/batchDeleteCustomer")
    public ResultObj batchDeleteCustomer(CustomerVo customerVo){
        try {
            List<Integer> IdList = new CopyOnWriteArrayList<>();
            for (Integer id : customerVo.getIds()) {
                IdList.add(id);
            }
            this.customerService.removeByIds(IdList);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

}

