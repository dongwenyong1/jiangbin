package com.itheima.health;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.utils.DateUtils;
import com.itheima.health.utils.SMSUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/5 15:07
 */
public class SMSTest {

    public static void main(String[] args) throws ClientException {
        SMSUtils.sendShortMessage("13574085230","1243");
    }



    @Test
    public  void  show() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date parse = simpleDateFormat.parse("1992-01");
        System.out.println(parse);


        try {
            System.out.println(DateUtils.parseDate2String(DateUtils.getToday(),"yyyy") );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
