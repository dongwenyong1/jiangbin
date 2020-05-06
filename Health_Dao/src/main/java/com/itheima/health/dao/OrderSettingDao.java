package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/4 11:37
 */
public interface OrderSettingDao {
    @Select("select count(*) from t_ordersetting where orderDate=#{orderDate}")
    long findCountByOrderDate(Date orderDate);

    @Update("update  t_ordersetting set number=#{number} where orderDate=#{orderDate} ")
    void update(OrderSetting orderSetting);

    @Insert("insert into t_ordersetting(orderDate,number,reservations) values(#{orderDate},#{number},#{reservations})")
    void add(OrderSetting orderSetting);

    @Select("select * from t_ordersetting where orderDate like concat(#{date},'%')")
    List<OrderSetting> getOrderSettingsByMoth(@Param("date") String date);

    @Select(" select * from t_ordersetting where orderDate between #{dateBegin} and #{dateEnd}")
    List<OrderSetting> getOrderSettingByMonth(Map map);

    @Select("select count(number) from t_ordersetting where orderDate=#{orderDate} ")
    long findNumberByOrdeDate(Date orderDate);

    @Select("select * from t_ordersetting where orderDate=#{date} ")
    OrderSetting findOrderSettingByDate(Date date);

    @Update("update t_ordersetting set reservations=reservations+1 where orderDate=#{date} ")
    void addSettingReservationsBydate(Date date);

    @Delete("delete from  t_ordersetting where orderDate<=#{month}")
    void removeByMonth(String month);
}
