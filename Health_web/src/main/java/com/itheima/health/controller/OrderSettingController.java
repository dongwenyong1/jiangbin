package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/4 11:06
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
         @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    //预约设置信息录入,用文件上传的对象multipartFile来接收
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        //poi读出的数据为list<[Object]>数组，每一个元素为一行数据

        try {
            //读取文件数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            //如果不为空则读取数据

            if (list!=null&&list.size()>0){
                //先创建一个集合来存储读出并封装的数据

                ArrayList<OrderSetting> orderSettingArrayList = new ArrayList<>();
                //遍历list读取数据并创建对象
                for (String[] strings : list) {
                    //读出数组中第一个和第二个数据并创建ordersetting对象
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                         orderSettingArrayList.add(orderSetting);
                }

                //调用service的方法进行存储orderSettingArrayList到数据库
                orderSettingService.add(orderSettingArrayList);
            }else {
                //文件为空
                return  new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL_NULL);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }//导入成功
        return  new Result(false, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

    }


    @RequestMapping("/getOrderSettingByMoth")//日期格式1990-01   返回data是map集合
    public Result getOrderSettingsByMoth(String date){
        //调用service的方法获取本月的数据
        try {
            List<Map> list=orderSettingService.getOrderSettingsByMoth1(date);
            return  new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //查询失败
            return  new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }
    /*通过点击日期设置可预约人数*/
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {

        try {
            orderSettingService.editNumberByDate(orderSetting);
            //预约成功
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }

    }








    /*    @RequestMapping("/getOrderSettingByMoth")//日期格式1990-01
    public Result getOrderSettingsByMoth(String date){
        //调用service的方法获取本月的数据
        try {
            List<OrderSetting> list=orderSettingService.getOrderSettingsByMoth(date);
            return  new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //查询失败
            return  new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }*/


  /*  @RequestMapping("/getOrderSettingByMoth")//日期格式1990-01  以1990-01开头查询  返回data是map集合
    public Result getOrderSettingsByMoth(String date){
        //调用service的方法获取本月的数据
        try {
            List<Map> list=orderSettingService.getOrderSettingsByMoth3(date);
            return  new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            //查询失败
            return  new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }*/



}
