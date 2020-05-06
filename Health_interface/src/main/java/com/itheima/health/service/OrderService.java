package com.itheima.health.service;

import com.itheima.health.enity.Result;

import java.util.Map; /**
 * @author ${dong}
 * @date 2020/1/5 16:18
 */
public interface OrderService {
    Result order(Map map) throws Exception;
    Map findById(Integer id);
}
