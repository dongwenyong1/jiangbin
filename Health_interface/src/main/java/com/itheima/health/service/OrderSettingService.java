package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/4 11:30
 */
public interface OrderSettingService {
    void add(ArrayList<OrderSetting> orderSettingArrayList);
    List<OrderSetting> getOrderSettingsByMoth(String date);
    List<Map> getOrderSettingsByMoth1(String date);
    List<Map> getOrderSettingsByMoth3(String date);

    void editNumberByDate(OrderSetting orderSetting);

    void removeOrder();
}
