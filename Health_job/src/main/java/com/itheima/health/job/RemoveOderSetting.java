package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ${dong}
 * @date 2020/1/12 17:03
 */


public class RemoveOderSetting {
    @Reference
  private   OrderSettingService orderSettingService;
    public void removeOrderSetting(){
        /*每月凌晨2点进行一次清理数据中预约列表的数据*/
        orderSettingService.removeOrder();
        System.out.println("定时任务=============================================web");
    }
}
