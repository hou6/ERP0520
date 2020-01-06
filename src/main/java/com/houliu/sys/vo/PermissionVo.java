package com.houliu.sys.vo;

import com.houliu.sys.entity.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houliu
 * @create 2019-12-30 16:32
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionVo extends Permission {

    private static final long serialVersionUID=1L;

    private Integer page = 1;
    private Integer limit = 10;

}
