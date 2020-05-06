package com.itheima.health.service;

import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.pojo.Permission;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 11:26
 */
public interface PermissionService {
    PagesResult findPage(QueryPageBean queryPageBean);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    void delete(Integer id);

    List<Permission>  findAll();
}
