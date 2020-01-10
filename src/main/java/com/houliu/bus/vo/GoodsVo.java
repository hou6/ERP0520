package com.houliu.bus.vo;

import com.houliu.bus.entity.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houliu
 * @create 2020-01-08 21:54
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsVo extends Goods {

    private static final Long serialVersionUID = 1L;

    private Integer page;
    private Integer limit;

}
