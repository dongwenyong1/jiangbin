package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.utils.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/5 16:19
 */
@Service(interfaceClass =OrderService.class )
@Transactional
public class OrderServiceImpl implements OrderService {
    //需要查询预约设置数据，所以需要orderSettingDao
    @Autowired
    private OrderSettingDao orderSettingDao;
    //需要查询会员信息，所以需要MemberDao
    @Autowired
    private MemberDao memberDao;
    //需要添加预约信息，所以需要orderDao
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        //处理预约信息:思路:查询当天是否进行了预约设置：
       String  orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        //通过日期获取预约orderSetting套餐信息
         OrderSetting orderSetting= orderSettingDao.findOrderSettingByDate(date);
        // 1.1.如果没有，则返回为设置预约信息的错误信息(所选的日期不能预约)，
        if (orderSetting==null){
            return  new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 1.2.查询当天已经可预约的人数
        int reservations = orderSetting.getReservations();//获取已经预定人数
        int number = orderSetting.getNumber();//获取可预约总人数
        if (number<=reservations){//1.2.1 没有，预约失败:已经预约满，不能预约
            return  new Result(false,MessageConstant.ORDER_FULL);

        }
        //    1.2.2 有,可预约人数:
        //          1.2.1.1：判断是不是会员：1.是会员： 防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约）：
         String telephone = (String) map.get("telephone");
         Member member= memberDao.findMemberByTel(telephone);

         if (member!=null){//会员存在，判断是否已经有预约
             Integer memberId = member.getId();//获取会员id
             // 获取套餐id
             int setmealId = Integer.parseInt((String)(map.get("setmealId")));
             //通过会员id和套餐id查询预约情况的list集合
             Order order = new Order(memberId, date, null, null, setmealId);
            List<Order> orders= orderDao.findByCondiction(order);//为了增加后续代码的复用性，可以传入一个oder，动态获取条件，获取结果
            if (orders!=null&&orders.size()>0){
                //已经有预约
                return  new Result(false,MessageConstant.HAS_ORDERED);
            }
         }
         if (member==null){
             //先要创建member数据库数据,并从map中获取数据并设置
             member = new Member();
             member.setName((String)map.get("name"));
             member.setPhoneNumber((String)map.get("telephone"));
             member.setIdCard((String)map.get("idCard"));
             member.setSex((String)map.get("sex"));
             member.setRegTime(new Date());
             //添加会员
             memberDao.add(member);
         }

        //2：==可预约人数减一

        orderSettingDao.addSettingReservationsBydate(date);
         //将此次要保存的数据加到order中,保存信息并添加到数据库的Order表中预约表
        Order order=new Order(member.getId(),date,(String)map.get("orderType"), Order.ORDERSTATUS_NO,Integer.parseInt((String)(map.get("setmealId"))));

         orderDao.add(order);

         //把预约信息返回给上一层
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer id) {

        return orderDao.findById(id);
    }
}
