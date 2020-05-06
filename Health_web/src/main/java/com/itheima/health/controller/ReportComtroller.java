package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.enity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ${dong}
 * @date 2020/1/8 9:20
 */
@RestController
@RequestMapping("/report")
public class ReportComtroller {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;

    //思路：1.获取当前月份，往前推一年，获取月份集合，
    //      2.传给service,查询每月数据并返回List<Integer>，
    //      3.将每月数据跟月份

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //获取当前日期的前一年的日期
     Map<String ,Object> map=memberService.getMemberReport();
       if (map!=null&&map.size()>0){
           return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
       }
      return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
    }



    @RequestMapping("/getMemberReportA")//按照某特定区间区间值
    public Result getMemberReportA(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        //获取当前日期的前一年的日期
        Map<String ,Object> map=memberService.getMemberReportA(startDate,endDate);

        if (map!=null&&map.size()>0){
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }
        return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
    }



    @RequestMapping("/getMemberReportBySex")
 public  Result    getMemberCountBySex(){

        Map<String,Objects> map=memberService. getMemberReportBySex();

        if (map!=null&&map.size()>0){
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }
        return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
 }

    @RequestMapping("/getMemberReportByAge")//0-18  18-30  30-45 45以上
    public  Result    getMemberCountByAge() throws Exception {
        Map<String,Objects> map=memberService. getMemberReportByAge();
        if (map!=null&&map.size()>0){
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        }
        return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
    }




    @RequestMapping("/getSetmealReport")
    public  Result getSetmealReport(){
        Map<String,Objects> map=setmealService.getSetmealReport();

        if (map!=null&&map.size()>0){
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,map);
        }
        return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);

    }
    @RequestMapping("/getBusinessReportData")
    public  Result getBusinessReportData() throws Exception {

        Map<String,Object> map=reportService.getSetmealReport();

        if (map!=null&&map.size()>0){
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,map);
        }
        return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);

    }


    @RequestMapping("exportBusinessReport")
    public  Result exportBusinessReport(HttpServletRequest request , HttpServletResponse response) {
        //获取相关数据，并调用POIUtils

        try {
            Map<String, Object> map = reportService.getSetmealReport();
            Integer today = (Integer) map.get("totalMember");
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            //获取 totalMember :
            Integer totalMember = (Integer) map.get("totalMember");
            //获取 thisWeekNewMember :1.获取周一  2.获取周日
            Integer  thisWeekMonday = (Integer) map.get("thisWeekMonday");
            Integer  thisWeekSundy = (Integer) map.get("thisWeekSundy");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");//本周新增的会员数
            //获取 thisMonthNewMember :
            //先获取本月的第一天，获取本月的最后一天
            Integer thisFirstDay4Month = (Integer) map.get("thisFirstDay4Month");
            Integer thisLastDay4Month = (Integer) map.get("thisLastDay4Month");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            //获取 todayOrderNumber :
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            //获取 todayVisitsNumber map.get("todayNewMember");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            //获取 thisWeekOrderNumber map.get("todayNewMember");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            //获取 thisWeekVisitsNumber map.get("todayNewMember");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            //获取 thisMonthOrderNumber map.get("todayNewMember");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            //获取 thisMonthVisitsNumber map.get("todayNewMember");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            //获取 hotSetmeal  取前四
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");


            //获取  获得Excel模板文件绝对路径
            String realPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = null;

            workbook = new XSSFWorkbook(new FileInputStream(new File(realPath)));

            //获取第一个sheet
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);//获取第二行
            row.getCell(5).setCellValue(today);                                 //设置值为日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for (Map map1 : hotSetmeal) {//热门套餐
                String name = (String) map1.get("name");
                Long setmeal_count = (Long) map1.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比

            }
            //通过输出流输出
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(outputStream);//直接使用wokbook的write方法（输出流）


        }catch(Exception e){
            e.printStackTrace();//获取表异常
            return  new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
      return  null;
        }

    }

