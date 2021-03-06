package com.houliu.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houliu.sys.entity.User;

/**
 * @author houliu
 * @create 2019-12-30 00:47
 */
public interface IUserService extends IService<User> {

    //保存用户和角色之间的关系
    void saveUserRole(Integer uid, Integer[] ids);

}
