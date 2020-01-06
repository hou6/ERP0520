package com.houliu.sys.vo;

import com.houliu.sys.entity.Notice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author houliu
 * @create 2019-12-31 23:03
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeVo extends Notice {

    private static final long serialVersionUID=1L;

    private Integer page = 1;
    private Integer limit = 10;

    private Integer[] ids;   //接收多个id，用于批量操作

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
