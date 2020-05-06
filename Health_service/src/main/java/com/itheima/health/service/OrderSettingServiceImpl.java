package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ${dong}
 * @date 2020/1/4 11:31
 */
@Service(interfaceClass =OrderSettingService.class )
@Transactional
public class OrderSettingServiceImpl implements  OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(ArrayList<OrderSetting> list) {
        //需要判断日期是否存在：1.存在-更新       2.不存在：保存
        if (list != null && list.size() > 0) {
            //遍历list通过日期查询数据 ，入有数据，就更新 没有就添加  :防止上传重复的内容
            for (OrderSetting orderSetting : list) {
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //表示有数据 需要更新
                    orderSettingDao.update(orderSetting);
                } else {
                    //表示没有数据，直接添加
                    orderSettingDao.add(orderSetting);
                }

            }

        }


    }

    @Override//直接调用dao层的方法  以date开头去找值,返回值为ordersetting集合
    public List<OrderSetting> getOrderSettingsByMoth(String date) {
        return orderSettingDao.getOrderSettingsByMoth(date);

    }

    @Override//先处理一下date数据，再去查询，返回值为map
    public List<Map> getOrderSettingsByMoth1(String date) {
        //开始日期
        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        //创建一个map集合用于传递参数
        Map map = new HashMap<>();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        //查询当月的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        // 3.将List<OrderSetting>，组织成List<Map>
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;

    }

    @Override
    public List<Map> getOrderSettingsByMoth3(String date) {//以1993-01开头
        List<OrderSetting> list = orderSettingDao.getOrderSettingsByMoth(date);
        List<Map> data = new ArrayList<>();
        // 3.将List<OrderSetting>，组织成List<Map>
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;

    }

    @Override//设置
    public void editNumberByDate(OrderSetting orderSetting) {
        //更改数据库中的可预约总数:思路：判断是需要修改还是新增

        long count= orderSettingDao.findNumberByOrdeDate(orderSetting.getOrderDate());

        //判断count大于0或者小于0
        if (count>0){//数据库已经有数据，更新数据据即可
               orderSettingDao.update(orderSetting);
        }else {
            //数据库没有数据，直接添加
           orderSettingDao.add(orderSetting);
        }

    }

    @Override//定期清理一个三个月前的数据
    public void removeOrder() {
        //获取当前日期

        Calendar cd = Calendar.getInstance();//获取当前时间
        cd.add(Calendar.MONTH,-36);//将时间往前推36个月
        //删除数据库的数据
        String month = new SimpleDateFormat("yyyy-MM").format(cd.getTime());
        Date date = new Date();
        String day=null;
        if (date.getDate()<9){
         day="0"+date.getDate();}
        else  day=""+date.getDate();
        orderSettingDao.removeByMonth(month+"-"+day);

    }


}
