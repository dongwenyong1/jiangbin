package com.itheima.health.dao;

import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @author ${dong}
 * @date 2020/1/6 16:07
 */
public interface RoleDao {

    Set<Role> findRolesByUid(Integer uid);

    List<Role> findRolesByCondition(String queryString);

    @Insert("insert into t_role(name,keyword,description) values( #{name},#{keyword},#{description})")
    @Options(useGeneratedKeys = true,keyColumn = "id")
    void add(Role role);

    @Insert("insert into t_role_menu (role_id,menu_id) values(#{rid},#{menuId})")
    void setRoleMenu(@Param("menuId") Integer menuId, @Param("rid") Integer rid);

    @Insert("insert into t_role_permission (role_id,permission_id) values(#{rid},#{permissionId})")
    void setRolePermission(@Param("permissionId") Integer permissionId, @Param("rid") Integer rid);

    @Select("select menu_id from  t_role_menu where role_id=#{id}")
    List<Integer> findMenuIdsByRoleId(Integer id);

    @Select("select permission_id from t_role_permission where role_id=#{id}")
    List<Integer> findPermisssionIdsByRoleId(Integer id);

    void edit(Role role);

    @Delete("delete from t_role_permission  where role_id=#{id}  ")
    void removeRoleAssociation2permission(Integer id);
    @Delete("delete from  t_role_menu  where role_id=#{id}  ")
    void removeRoleAssociation2Menu(Integer id);
    @Delete("delete  from t_role  where id=#{id}  ")
    void delete(Integer id);

    @Select("select * from t_role  where id=#{id} ")
    Role indRoleById(Integer id);

    @Select("select * from t_role ")
    List<Role> findAll();
    @Select("SELECT m.* FROM t_menu AS m ,t_role_menu AS rm WHERE m.level=1 AND m.id=rm.menu_id  AND role_id=#{id}")
    List<Integer> findMenuIdsByRolePid(Integer id);
}
