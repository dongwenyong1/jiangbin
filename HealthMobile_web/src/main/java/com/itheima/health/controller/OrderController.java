package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/5 15:29
 */
@RestController
@RequestMapping("/order")
public class OrderController {
     @Reference
     private OrderService orderService;
     @Autowired
     private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result order(@RequestBody Map map){//获取的参数是一个列表信息，可以用一个map进行封装
      //从redis中获取验证码，key值为电话号码+RedisMessageConstant.sendOrder
      String  telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
         //获取填写的验证码
        Object validateCode = map.get("validateCode");

        //如果验证码匹配成功，进行下一步
     /*   if (codeInRedis==null||!codeInRedis.equals(validateCode)){
            return  new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }*/

        //定义一个空的结果集：service层出现异常，预约失败，返回null
        Result result = null;
        try {
            //调用体验预约服务
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//采用微信预约方式预约
            //调用service的预约方法，
            result= orderService.order(map);

        } catch (Exception e) {
            e.printStackTrace();//预约失败
            return  result;
        }

        if (result.isFlag()){
            //预约成功，//预约成功，发送短信通知，短信通知内容可以是
            // “预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
          String orderDate = (String) map.get("orderDate");
         //调用SMSUtils发送短信
            try {
                SMSUtils.sendShortMessage(telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }

        }
     return  result;
    }
     @RequestMapping("/findById")
    public Result findById(Integer id){

         try {
             Map map= orderService.findById(id);
             return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
         } catch (Exception e) {
             e.printStackTrace();
             //yo有异常
             return  new Result(false,MessageConstant.QUERY_ORDER_FAIL);
         }


     }





}
