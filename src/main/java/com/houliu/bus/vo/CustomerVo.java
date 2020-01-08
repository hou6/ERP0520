package com.houliu.bus.vo;

import com.houliu.bus.entity.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houliu
 * @create 2020-01-07 15:58
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerVo extends Customer {

    private static final Long serialVersionUID = 1L;

    private Integer page = 1;
    private Integer limit = 10;

    private Integer[] ids;



}
