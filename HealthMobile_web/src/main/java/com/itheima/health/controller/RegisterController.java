package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.apache.zookeeper.server.SessionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/14 19:19
 */
@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    JedisPool jedisPool;
    @Autowired
    UserService userService;


    //用户注册使用
//因有验证码 所有用map来接收
    public Result  userRegister(HttpServletResponse response, HttpServletRequest request, @RequestBody Map map){
        //检验验证码   键为电话号码+验证码类型
        String telephone = (String) map.get("telephone");
        String code = (String) map.get("validateCode");//用户输入的验证码

        String validateCode = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_REGISTER);

        //如果验证码匹配成功
        if (code.equals(telephone)) {
         User user=   userService.register(map);
         //存到session中
            request.getSession().setAttribute("user",user);

         return new Result(true,MessageConstant.ADD_USER_SUCCESS,user);
        }else return new Result(false, MessageConstant.VALIDATECODE_ERROR);


    }


}
