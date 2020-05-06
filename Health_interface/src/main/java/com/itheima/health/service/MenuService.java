package com.itheima.health.service;

import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 15:39
 */
public interface MenuService {
    PagesResult findPage(QueryPageBean queryPageBean);

    void add( Menu  menu);

   Menu findById(Integer id);

    void edit(Menu menu);

    void delete(Integer id);

    List<Menu> findAll();
}
