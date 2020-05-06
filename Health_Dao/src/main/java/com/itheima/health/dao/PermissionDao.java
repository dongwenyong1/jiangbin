package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/6 17:22
 */
public interface PermissionDao {
    @Select("  SELECT p.* FROM t_permission AS p,t_role_permission AS  r  WHERE r.permission_id=p.id AND r.role_id=#{rid}")
   List<Permission> findPermissionByRoleId(Integer rid);


    List<Permission> findByCondiction(String queryString);

    @Insert("insert into t_permission(name,keyword,description) values(#{name},#{keyword},#{description}) ")
    void add(Permission permission);


    @Select("select * from  t_permission where id=#{id}")
    Permission findById(Integer id);


    void edit(Permission permission);

    @Select("select count(*) from t_role_permission where permission_id=#{id}")
    Long findAssociationCountWithRole(Integer id);

    @Delete("delete  from t_permission where id=#{id} ")
    void deleteById(Integer id);

 @Select("select * from  t_permission ")
    List<Permission> findAll();
}
