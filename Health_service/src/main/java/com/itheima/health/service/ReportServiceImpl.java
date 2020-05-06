package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ${dong}
 * @date 2020/1/8 17:39
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public Map<String, Object> getSetmealReport() throws Exception {

        //获取 todayNewMember :
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        Integer todayNewMember = memberDao.findMemberByDate(today);
        //获取 totalMember :
        Integer totalMember = memberDao.findAllMemberCount();
        //获取 thisWeekNewMember :1.获取周一  2.获取周日
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        String thisWeekSundy = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());

        Integer thisWeekNewMember = memberDao.findMemberByAfterDate(thisWeekMonday);//本周新增的会员数
        //获取 thisMonthNewMember :
        //先获取本月的第一天，获取本月的最后一天
        String thisFirstDay4Month = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        String thisLastDay4Month = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
        Integer thisMonthNewMember = memberDao.findMemberByAfterDate(thisFirstDay4Month);
        //获取 todayOrderNumber :
         Integer   todayOrderNumber=orderDao.findOrdersByAfterDate(today);
        //获取 todayVisitsNumber :
        Integer todayVisitsNumber=orderDao.findAlreadyVisitOrderAfterDate(today);
        //获取 thisWeekOrderNumber :
        Integer thisWeekOrderNumber=orderDao.findOrderNumberByArea(thisWeekMonday,thisWeekSundy);
               //获取 thisWeekVisitsNumber :
        Integer thisWeekVisitsNumber=orderDao.findVisitNumberByArea(thisWeekMonday,thisWeekSundy);
               //获取 thisMonthOrderNumber :
        Integer thisMonthOrderNumber =orderDao.findOrderNumberByArea(thisFirstDay4Month,thisLastDay4Month);
               //获取 thisMonthVisitsNumber :
        Integer thisMonthVisitsNumber =orderDao.findVisitNumberByArea(thisFirstDay4Month,thisLastDay4Month);
               //获取 hotSetmeal  取前四
        List<Map> hotSetmeal=orderDao.findHotSetmeal();

        Map<String, Object> result= new HashMap<>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);


        return result;
    }
}
