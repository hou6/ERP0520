package com.houliu.sys.vo;

import com.houliu.sys.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author houliu
 * @create 2020-01-05 14:25
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo extends User {

    private static final Long serialVersionUID = 1L;

    private Integer page = 1;
    private Integer limit = 10;

}
