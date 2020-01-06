package com.houliu.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.houliu.sys.common.DataGridView;
import com.houliu.sys.common.ResultObj;
import com.houliu.sys.common.WebUtils;
import com.houliu.sys.entity.Notice;
import com.houliu.sys.entity.User;
import com.houliu.sys.service.INoticeService;
import com.houliu.sys.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-25
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    /**
     * 查询
     * @param noticeVo
     * @return
     */
    @RequestMapping("loadAllNotice")
    public DataGridView loadAllNotice(NoticeVo noticeVo){
        IPage<Notice> page = new Page<>(noticeVo.getPage(),noticeVo.getLimit());
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),"title",noticeVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername",noticeVo.getOpername());
        queryWrapper.like(noticeVo.getStartTime() != null,"startTime",noticeVo.getStartTime());
        queryWrapper.like(noticeVo.getEndTime() != null,"endTime",noticeVo.getEndTime());
        queryWrapper.orderByDesc("createtime");
        this.noticeService.page(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加
     * @param noticeVo
     * @return
     */
    @RequestMapping("addNotice")
    public ResultObj addNotice(NoticeVo noticeVo){
        try {
            noticeVo.setCreatetime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            noticeVo.setOpername(user.getName());
            this.noticeService.save(noticeVo);
            return ResultObj.ADD_OK;
        } catch (Exception e) {
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     * @param noticeVo
     * @return
     */
    @RequestMapping("updateNotice")
    public ResultObj updateNotice(NoticeVo noticeVo){
        try {
            this.noticeService.updateById(noticeVo);
            return ResultObj.UPDATE_OK;
        } catch (Exception e) {
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("deleteNotice")
    public ResultObj deleteNotice(Integer id){
        try {
            this.noticeService.removeById(id);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 根据id批量删除
     * @param noticeVo
     * @return
     */
    @RequestMapping("/batchDeleteNotice")
    public ResultObj batchDeleteNotice(NoticeVo noticeVo){
        try {
            List<Integer> IdList = new CopyOnWriteArrayList<>();
            for (Integer id : noticeVo.getIds()) {
                IdList.add(id);
            }
            this.noticeService.removeByIds(IdList);
            return ResultObj.DELETE_OK;
        } catch (Exception e) {
            return ResultObj.DELETE_ERROR;
        }
    }

}

