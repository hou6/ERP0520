package com.houliu.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houliu.bus.entity.Outport;

/**
 * <p>
 * InnoDB free: 9216 kB 服务类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-19
 */
public interface IOutportService extends IService<Outport> {

    /**
     * //添加退货信息
     * @param id              进货单ID
     * @param number          进货数量
     * @param remark          备注
     */
    void addOutPort(Integer id, Integer number, String remark);
}
