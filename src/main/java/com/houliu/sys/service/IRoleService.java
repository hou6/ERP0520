package com.houliu.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.houliu.sys.entity.Role;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB 服务类
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-28
 */
public interface IRoleService extends IService<Role> {

    /**
     * 根据角色ID查询当前用户的菜单和权限ID
     * @param roleId
     * @return
     */
    List<Integer> queryRolePermissionIdsByRid(Integer roleId);

    /**
     * 保存菜单权限和角色之间的关系
     * @param rid
     * @param ids
     */
    void saveRolePermission(Integer rid, Integer[] ids);
}
