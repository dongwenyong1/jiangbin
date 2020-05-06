package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;

/**
 * @author ${dong}
 * @date 2020/1/6 9:10
 */
public interface MemberService {
    public Member findByTelephone(String telephone) ;
    public void add(Member member) ;


    Map<String,Object> getMemberReport();


    Map<String,Object> getMemberReportA(String startDate, String endDate) ;

    Map<String,Objects> getMemberReportBySex();

    Map<String,Objects> getMemberReportByAge() throws Exception;
}
