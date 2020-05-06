package com.itheima.health.service;

import com.itheima.health.pojo.CheckItem;
import com.itheima.health.enity.PagesResult;

import java.util.List;

/**
 * @author ${dong}
 * @date 2019/12/31 16:59
 */
public interface CheckItemService {
    void add(CheckItem checkItem);

    PagesResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void deleteById(Integer id);

    CheckItem findItemById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();
}
