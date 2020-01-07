package com.houliu.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houliu.sys.entity.Role;
import com.houliu.sys.mapper.RoleMapper;
import com.houliu.sys.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-28
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    /**
     * 重写removeById，删除中间表的一些数据
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        //删除sys_role_permission中间表
        this.baseMapper.deleteRolePermissionByRid(id);
        //删除sys_role_user中间表
        this.baseMapper.deleteRoleUserByRid(id);
        return super.removeById(id);
    }

    /**
     * 根据角色ID查询当前用户的菜单和权限ID
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {
        return this.baseMapper.queryRolePermissionIdsByRid(roleId);
    }

    /**
     * 保存菜单权限和角色之间的关系
     * @param rid
     * @param ids
     */
    @Override
    public void saveRolePermission(Integer rid, Integer[] ids) {
        RoleMapper roleMapper = this.getBaseMapper();
        //根据rid删除sys_role_permisssion,保存新的之前先把之前的删除
        roleMapper.deleteRolePermissionByRid(rid);
        if (ids != null && ids.length > 0){
            for (Integer pid : ids) {
                roleMapper.saveRolePermission(rid,pid);
            }
        }
    }

    /**
     * 询当前用户所拥有的角色id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> queryUserRoleIdsByUid(Integer id) {
        return this.getBaseMapper().queryUserRoleIdsByUid(id);
    }

}
