package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ${dong}
 * @date 2020/1/6 9:20
 */
@Transactional
@Service(interfaceClass =  MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findMemberByTel(telephone);
    }
    @Override
    public void add(Member member) {
         memberDao.add(member);
    }



    //思路：1.获取当前月份，往前推一年，获取月份集合，
    //      2.传给service,查询每月数据并返回List<Integer>，
    //      3.将每月数据跟月份
    @Override
    public Map<String, Object> getMemberReport() {
        Calendar cd=Calendar.getInstance();//获取当前日历
         //cd.add(Calendar.MONTH,-12);//获取前十二个月的日期
                                  //格式yyyy-mm
        DateFormat format = new SimpleDateFormat("yyyy-MM");
        List<String> mothList=new ArrayList<>();
        List<Integer>  memberCount=  new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            cd.add(Calendar.MONTH,-1);//每次减少一个1
            String month = format.format(cd.getTime());//获取月份时间
           Integer memberCountByMonth=memberDao.findMemberByMonth(month);
              memberCount.add(memberCountByMonth);
              mothList.add(month);//月份列表添加
        }
        Collections.reverse(mothList);
        Collections.reverse(memberCount);
        //创建map集合
        Map<String,Object> map =new HashMap<>();
        map.put("months",mothList);
        map.put("memberCount",memberCount);
        return map;
    }

    @Override
    public Map<String, Object> getMemberReportA(String startDate, String endDate)  {
        List<Integer>  memberCount=  new ArrayList<>();
        List<String> mothList = getMothList(startDate, endDate);//调用方法获取月份集合
        //遍历并获取数据
        for (String s : mothList) {
            memberCount.add(memberDao.findMemberByMonth(s));//将查询的数据封装到membercount中
        }
        Map<String,Object> map =new HashMap<>();
        map.put("months",mothList);
        map.put("memberCount",memberCount);
        return map;

    }

    @Override//按照性别：男、女
    public Map<String, Objects> getMemberReportBySex() {
     //获取会员性别及数量:集合： memberCount(男：122，女：122）
        List<Map<String ,Object>> list=memberDao.getMemberReportBySex();
        //获取类别名称：集合：memberNames男-女
        List<String > memberNames=new ArrayList<>();
        for (Map<String, Object> map : list) {
            memberNames.add((String)map.get("sex"));
        }
     Map resultMap=new HashMap();
        resultMap.put("memberNames",memberNames);
        resultMap.put("memberCount",list);
        return resultMap;
    }

    @Override   //区间：0-18  18-30  30-45 45以上
    public Map<String, Objects> getMemberReportByAge() throws Exception {
        List<String > memberNames=new ArrayList<>();
        String yyyy = DateUtils.parseDate2String(DateUtils.getToday(), "yyyy");//获取当前时间并转为2020格式
         int year=Integer.parseInt(yyyy);
        memberNames.add((year-18)+"-"+(year-0));
        memberNames.add((year-30)+"-"+(year-18));
        memberNames.add((year-45)+"-"+(year-30));
        memberNames.add((year-160)+"-"+(year-45));
        List<Map<String ,Object>> list=new ArrayList<>();
        for (String name : memberNames) {
           Map<String ,Object> map=new HashMap<>();
            map.put("value",memberDao.getMemberReportByAge( ""+Integer.parseInt(name.split("-")[0]),""+Integer.parseInt(name.split("-")[1])));
            map.put("name",name);
             list.add(map);
        }

        Map resultMap=new HashMap();
        resultMap.put("memberNames",memberNames);
        resultMap.put("memberCount",list);
        return resultMap;
    }


    private List<String> getMothList(String startTime, String endTime ){

        String y1 = startTime;// 开始时间
        String y2 = endTime;// 结束时间
        List<String> list=null;
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM").parse(y1);
            Date endDate = new SimpleDateFormat("yyyy-MM").parse(y2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            // 获取开始年份和开始月份
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH);
            // 获取结束年份和结束月份
            calendar.setTime(endDate);
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH);
            //
             list = new ArrayList<String>();
            for (int i = startYear; i <= endYear; i++) {
                String date = "";
                if (startYear == endYear) {
                    for (int j = startMonth; j <= endMonth; j++) {
                        if (j < 9) {
                            date = i + "-0" + (j + 1);
                        } else {
                            date = i + "-" + (j + 1);
                        }
                        list.add(date);
                    }

                } else {
                    if (i == startYear) {
                        for (int j = startMonth; j < 12; j++) {
                            if (j < 9) {
                                date = i + "-0" + (j + 1);
                            } else {
                                date = i + "-" + (j + 1);
                            }
                            list.add(date);
                        }
                    } else if (i == endYear) {
                        for (int j = 0; j <= endMonth; j++) {
                            if (j < 9) {
                                date = i + "-0" + (j + 1);
                            } else {
                                date = i + "-" + (j + 1);
                            }
                            list.add(date);
                        }
                    } else {
                        for (int j = 0; j < 12; j++) {
                            if (j < 9) {
                                date = i + "-0" + (j + 1);
                            } else {
                                date = i + "-" + (j + 1);
                            }
                            list.add(date);
                        }
                    }

                }

            }

    }catch (Exception e) {
            e.printStackTrace();
        }

return list;

}


}
