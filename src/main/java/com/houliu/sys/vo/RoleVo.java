package com.houliu.sys.vo;

import com.houliu.sys.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author houliu
 * @create 2020-01-04 17:34
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleVo extends Role {

    private static final Long serialVersionUID = 1L;

    private Integer page = 1;
    private Integer limit = 10;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
