package com.houliu.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.houliu.sys.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB Mapper 接口
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-28
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {


    void deleteRolePermissionByRid(@Param("id") Serializable id);

    void deleteRoleUserByRid(@Param("id") Serializable id);

    //根据角色ID查询当前用户的菜单和权限ID
    List<Integer> queryRolePermissionIdsByRid(@Param("roleId") Integer roleId);

    //保存菜单权限和角色之间的关系
    void saveRolePermission(@Param("rid") Integer rid, @Param("pid") Integer pid);

    void deleteRoleUserByUid(@Param("id") Serializable id);

    List<Integer> queryUserRoleIdsByUid(@Param("id") Integer id);

    void insertUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);
}
