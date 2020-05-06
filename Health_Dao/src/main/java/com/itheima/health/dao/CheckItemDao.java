package com.itheima.health.dao;

import com.itheima.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/1 0:42
 */
@Repository
public interface CheckItemDao {
    @Insert("insert into t_checkitem(code,name,sex,age,price,type,remark,attention) values (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention}) ")
   // @Insert("insert into t_checkitem(code,name,sex,age,price,type,remark,attention) values(#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")
    void add(CheckItem checkItem);

    List<CheckItem> selectBycondiction(String queryString);

    @Delete("delete from t_checkitem where id=#{id}")
    void deleteById(Integer id);

    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id=#{id}")
    long findCountBycheckItemId(Integer id);

 @Select("select * from  t_checkitem where id=#{id} ")
    CheckItem findItemById(Integer id);


    void update(CheckItem checkItem);

    @Select("select * from t_checkitem ")
    List<CheckItem> findAll();

    //通过检查组的id查询检查项集合
    @Select("SELECT * FROM t_checkitem AS ci WHERE ci.id IN(SELECT checkitem_id FROM t_checkgroup_checkitem WHERE checkgroup_id=#{id} ) ")
    List<CheckItem> findCheckItemListById();
}
