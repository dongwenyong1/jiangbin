package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/6 8:44
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    JedisPool jedisPool;

    @Reference
    MemberService memberService;

    //校验验证码和会员信息
    @RequestMapping("/check")
    public Result checkCode(HttpServletResponse response, @RequestBody Map map){
        //获取电话验证码
       String telephone = (String) map.get("telephone");
       String validateCode = (String) map.get("validateCode");
       //从jedis缓存中获取验证码
        String codeInredis= jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //判断验证码是否匹配
        if (codeInredis==null&&!codeInredis.equals(validateCode)){
          //不能匹配
            return  new Result(false, MessageConstant.VALIDATECODE_ERROR);

        }//验证成功，效验用户，通过telephone查询用户

       Member member= memberService.findByTelephone(telephone);

         //不是会员自动注册会员，
         if (member==null){
             member=new Member();
             member.setPhoneNumber(telephone);//手机号
             member.setRegTime(new Date());//注册日期
             memberService.add(member);
         }
      //是会员存储信息到cookie中

        Cookie member_login_telephone = new Cookie("Member_login_telephone", telephone);
         member_login_telephone.setPath("/");//所有路径均可得到
         member_login_telephone.setMaxAge(5*60*60);
         response.addCookie(member_login_telephone);

       return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }


}
