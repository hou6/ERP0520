package com.houliu.bus.controller;


import com.houliu.bus.service.IOutportService;
import com.houliu.sys.common.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/outport")
public class OutportController {

    @Autowired
    private IOutportService outportService;
    /**
     * 添加退货信息
     * @param id
     * @param number
     * @param remark
     * @return
     */
    @RequestMapping("addOutport")
    public ResultObj addOutport(Integer id,Integer number,String remark){
        try {
            this.outportService.addOutPort(id,number,remark);
            return ResultObj.OPERATE_OK;
        }catch (Exception e){
            return ResultObj.OPERATE_ERROR;
        }

    }

}

