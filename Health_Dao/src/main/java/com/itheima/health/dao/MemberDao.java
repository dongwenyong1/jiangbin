package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
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
public interface MemberDao {
    @Select("select * from t_member where phoneNumber=#{telephone}")
    Member findMemberByTel(String telephone);

    @Insert("insert into t_member(fileNumber,name,sex,idCard,phoneNumber,regTime,password,email,birthday,remark) " +
            "values(#{fileNumber},#{name},#{sex},#{idCard},#{phoneNumber},#{regTime},#{password},#{email},#{birthday},#{remark}) ")
    @Options(useGeneratedKeys = true,keyColumn = "id")//插入之后获取自动生成的id
    void add(Member member);

    @Select("select count(*) from t_member where regTime like concat(#{month},'%')")
    Integer findMemberByMonth(String month);

    @Select(" select count(id) from t_member where regTime <=#{value} ")
    Integer findMemberByDate(String today);

    @Select("select count(id) from t_member")
    Integer findAllMemberCount();

    @Select("select count(id) from t_member where regTime >= #{value} ")//日期小于某日
    Integer findMemberByAfterDate(String thisWeekMonday);

     @Select("SELECT sex as name , COUNT(id) as value FROM t_member GROUP BY sex")
    List<Map<String,Object>> getMemberReportBySex();

   @Select("SELECT COUNT(*) as value FROM t_member WHERE  birthday>#{a} AND birthday<#{b}")
    Integer getMemberReportByAge(@Param("a") String a, @Param("b") String b);
}
