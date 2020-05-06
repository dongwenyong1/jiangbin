package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/6 16:05
 */
public interface UserDao {

    User findUserByUsername(String username);

    List<User> findPage(String queryString);

    @Insert("insert into t_user(birthday,gender,username,password,remark,station,telephone) values(  #{birthday},#{gender},#{username},#{password},#{remark},#{station},#{telephone})")
    @Options(useGeneratedKeys = true,keyColumn = "id")
    void add(User user);


    @Select("select * from t_user where id=#{id}")
    User findUserById(Integer id);

    @Select("select count(*) from t_user_role where user_id=#{id}")
    long findUserRoleCountByUid(Integer id);

    @Delete("delete from t_user where id=#{id} ")
    void delete(Integer id);
    @Delete("delete from t_user_role where user_id=#{id} ")
    void removeUserAssociationWithRoleByUid(Integer id);
   @Insert("insert into t_user_role(user_id,role_id)  values (#{user_id},#{role_id})")
    void setUserAssociationWithRole(@Param("user_id") Integer id, @Param("role_id") Integer roleId);

    void edit(User user);
    @Insert("insert into t_user(birthday,gender,username,password,remark,station,telephone) values(#{birthday},#{gender},#{username},#{password},#{remark},#{station},#{telephone})")
    void register(Map map);
    @Select("select * from t_user where telephone=#{telephone}")
    User findUserByTelephone(String telephone);
}
