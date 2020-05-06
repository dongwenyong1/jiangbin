package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/5 17:50
 */
public interface OrderDao {
    @Insert("insert into t_order(member_id,orderDate,orderType,orderStatus,setmeal_id) values(#{memberId},#{orderDate},#{orderType},#{orderStatus},#{setmealId})")
    @Options(useGeneratedKeys = true,keyColumn = "id")//插入之后获取自动生成的id
    void add(Order order);

    //动态sql查询
    List<Order> findByCondiction(Order order);

    Map findById(Integer id);

     @Select("select count(id) from  t_order where orderDate=#{value}")
    Integer findOrdersByAfterDate(String date);

    @Select("select count(id) from  t_order where orderDate=#{value} and orderStatus='已到诊'")
    Integer findAlreadyVisitOrderAfterDate(String date);


    @Select("select count(id) from  t_order where orderDate between #{begin} and #{end}")
    Integer findOrderNumberByArea(@Param("begin") String begin,  @Param("end") String end);

    @Select("select count(id) from  t_order where orderDate between #{begin} and #{end} and orderStatus='已到诊'")
    Integer findVisitNumberByArea(@Param("begin") String begin,  @Param("end") String end);

   /* @Select(" SELECT s.name ,t.number AS setmeal_id ,t.number/(SELECT COUNT(id) FROM t_order) AS proportion  FROM t_setmeal AS  s,(SELECT setmeal_id,  COUNT(*) AS number FROM t_order GROUP BY setmeal_id) AS t \n" +
            " WHERE t.setmeal_id=s.id ORDER BY t.number DESC LIMIT 0,4")*/
   @Select("select s.name, count(o.id) setmeal_count ,count(o.id)/(select count(id) from t_order) proportion from t_order o inner join t_setmeal s on s.id = o.setmeal_id group by s.name order by setmeal_count desc limit 0,4")
    List<Map> findHotSetmeal();
}
