package com.houliu.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houliu.sys.entity.Permission;
import com.houliu.sys.mapper.PermissionMapper;
import com.houliu.sys.service.IPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 9216 kB 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-22
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Override
    public boolean removeById(Serializable id) {
        PermissionMapper permissionMapper = this.baseMapper;
        permissionMapper.deleteRolePermissionByPid(id);
        return super.removeById(id);
    }
}
