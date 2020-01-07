package com.houliu.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houliu.sys.entity.User;
import com.houliu.sys.mapper.RoleMapper;
import com.houliu.sys.mapper.UserMapper;
import com.houliu.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author houliu
 * @create 2019-12-30 00:48
 */

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    @Override
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeById(Serializable id) {
        //删除用户角色中间表数据
        roleMapper.deleteRoleUserByUid(id);
        //删除用户头像[如果是默认头像，则不删]
        return super.removeById(id);
    }

    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param ids
     */
    @Override
    public void saveUserRole(Integer uid, Integer[] ids) {
        //先删除用户和角色之间的关系
        this.roleMapper.deleteRoleUserByUid(uid);
        if (null != ids && ids.length > 0){
            for (Integer rid : ids) {
                this.roleMapper.insertUserRole(uid,rid);
            }
        }
    }
}
