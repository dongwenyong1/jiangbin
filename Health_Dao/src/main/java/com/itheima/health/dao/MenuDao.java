package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 15:52
 */
public interface MenuDao {

    List<Menu> findByCondition(String queryString);

    @Insert("insert into t_menu(name,linkUrl,path,priority,description,icon,parentMenuId,level) values(#{name},#{linkUrl},#{path},#{priority},#{description},#{icon},#{parentMenuId},#{level})")
    void add(Menu menu);

    @Select("select * from t_menu where id=#{id}")
    Menu findById(Integer id);
    void edit(Menu menu);
    @Select("select count(*) from t_role_menu where menu_id=#{id}")
    long findRoleCountByMenuId(Integer id);

    @Delete("delete from t_menu where id=#{id} ")
    void deleteById(Integer id);
    @Select("select * from t_menu ")
    List<Menu> finAll();

    @Select("select * from t_menu where id=#{cid} and parentMenuId=#{pid}")
    Menu findByPidAndCid(@Param("pid") Integer pid, @Param("cid")Integer cid);
}
