package com.itheima.health.dao;

import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/3 0:29
 */
public interface SetmealDao {

    @Insert("insert into  t_setmeal (name,code,helpCode,sex,age,price,remark,attention,img) values(#{name },#{code},#{helpCode},#{sex},#{age},#{price},#{remark},#{attention},#{img})")
    @Options(useGeneratedKeys = true,keyColumn = "id")
    void add(Setmeal setmeal);
     @Insert("insert into t_setmeal_checkgroup (setmeal_id,checkgroup_id) values(#{setmeal_id},#{checkgroup_id}) ")
    void setSetmealAndCheckGroup(@Param("setmeal_id") Integer setmeaId, @Param("checkgroup_id")  Integer groupId);

    List<Setmeal> selectCountByCondition(String queryString);
    List<Setmeal> selectCountByConditionlike(String queryString);

    @Select("select * from t_setmeal where  id=#{id}")
    Setmeal findSetmealById(Integer id);

    @Select("select  checkgroup_id   from t_setmeal_checkgroup where  setmeal_id=#{id}")
    List<Integer> findCheckGroupIdsBysetmealId(Integer id);

    @Delete("delete  from t_setmeal_checkgroup where  setmeal_id=#{id}")
    void deleteAssociationWithCheckGroupBySid(Integer id);

    void edit(Setmeal setmeal);
    @Delete("delete from   t_setmeal where  id=#{id}")
    void deleteById(Integer id);
    @Select("select * from t_setmeal")
    List<Setmeal> findAll();

    //通过套餐id查询套餐的信息
    Setmeal findSetmealResultMapById(Integer id);

    @Select("SELECT	s.name,COUNT(o.id) AS value FROM t_order AS o,t_setmeal AS s WHERE o.setmeal_id=s.id GROUP BY s.name ")
      List<Map<String,Object>>   getSetmealReport();
}
