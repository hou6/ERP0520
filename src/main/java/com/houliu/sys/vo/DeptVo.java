package com.houliu.sys.vo;

import com.houliu.sys.entity.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houliu
 * @create 2020-01-02 16:08
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class DeptVo extends Dept {

    private static final Long serialVersionUID = 1L;

    private Integer page = 1;
    private Integer limit = 10;

}
