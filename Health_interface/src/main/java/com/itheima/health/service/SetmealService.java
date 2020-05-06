package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ${dong}
 * @date 2020/1/3 0:24
 */

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] checkGroupIds);

    PagesResult findPage(Integer currentPage, Integer pageSize, String queryString);

    Setmeal findSetmealById(Integer id);

    List<Integer> findCheckGroupIdsBysetmealId(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void deleteById(Integer id);

    List<Setmeal> findAll();

    Setmeal findSetmealResultMapById(Integer id);

    Map<String,Objects> getSetmealReport();
}
