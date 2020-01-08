package com.houliu.bus.vo;

import com.houliu.bus.entity.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houliu
 * @create 2020-01-08 20:29
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ProviderVo extends Provider {

    private static final Long serialVersionUID = 1L;

    private Integer page;
    private Integer limit;

    //用于接收多个id
    private Integer[] ids;

}
