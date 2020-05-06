package com.itheima.health.service;

import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.pojo.Role;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 17:34
 */
public interface RoleService {
    PagesResult findPage(QueryPageBean queryPageBean);

    void add(Integer[] menuIds, Integer[] permissionIds, Role role);

    List<Integer> findMenuIdsByRoleId(Integer id);

    List<Integer> findPermisssionIdsByRoleId(Integer id);

    void edit(Integer[] menuIds, Integer[] permissionIds, Role role);

    void deleteByRoleId(Integer id);

    Role findRoleById(Integer id);

    List<Role> findAll();
}
