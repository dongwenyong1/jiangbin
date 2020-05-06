package com.itheima.health.service;

import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/6 16:03
 */
public interface UserService {
    User findUserByUsername(String username);

    PagesResult findPage(QueryPageBean queryPageBean);

    void add(User user, Integer[] roleIds);

    void edit(User user, Integer[] roleIds);

    User findUserById(Integer id);

    void delete(Integer id);

    Result getUserMenusByUsername(String username);

    User register(Map map);
}
