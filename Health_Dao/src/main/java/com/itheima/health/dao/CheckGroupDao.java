package com.itheima.health.dao;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/2 9:38
 */
public interface CheckGroupDao {
    //需要查询最快添加的id
    @Insert("insert into t_checkGroup (id,code,name,helpCode,sex,remark,attention) values(#{id},#{code},#{name},#{helpCode},#{sex},#{remark},#{attention})")
    @Options(useGeneratedKeys = true,keyColumn = "id")
    void add(CheckGroup checkGroup);

    @Insert("insert into t_checkgroup_checkitem(checkgroup_id,checkitem_id) values(#{checkgroup_id},#{checkitem_id})")
    void setCheckGroupAndCheckItem(@Param("checkgroup_id") Integer checkGroupId,@Param("checkitem_id") Integer checkitemId);

    List<CheckGroup> selectByCondition(String queryString);

    @Select("select * from t_checkgroup where id=#{id} ")
    CheckGroup findCheckGroupById(Integer id);
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id}")
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    @Delete("DELETE  FROM t_checkgroup_checkitem WHERE checkgroup_id=#{id}")
    void deleteAssociation(Integer id);
    Integer edit(CheckGroup checkGroup);

    @Delete("delete from t_checkgroup where id=#{id}")
    void deleteCheckGroup(Integer id);

    @Select("select * from  t_checkgroup ")
    List<CheckGroup> findAll();

    //通过套餐组的id套餐所包含的所有检查组
    List<CheckGroup> findCheckGroupListById(Integer id);
}
