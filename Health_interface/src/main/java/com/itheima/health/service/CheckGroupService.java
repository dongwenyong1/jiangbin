package com.itheima.health.service;

import com.itheima.health.enity.PagesResult;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/2 9:35
 */
public interface CheckGroupService {
    void add(CheckGroup checkGroup , Integer[] checkitemIds);

    PagesResult findPage(Integer currentPage, Integer pageSize, String queryString);

    CheckGroup findCheckGroupById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void delete(Integer id);

    List<CheckGroup> findAll();
}
