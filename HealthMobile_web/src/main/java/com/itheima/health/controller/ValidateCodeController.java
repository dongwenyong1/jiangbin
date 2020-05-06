package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.enity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author ${dong}
 * @date 2020/1/5 10:44
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

  @RequestMapping("/send4Order")
 public Result validateCode4Order(String telephone){
      Integer code = ValidateCodeUtils.generateValidateCode(4);//生成验证码int 4位
     //调用发送短信
      try {
          SMSUtils.sendShortMessage(telephone,code.toString());
      } catch (ClientException e) {
          e.printStackTrace();//发送失败
          return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
      }
    //将生成的验证码缓存到redis,保存时间5分钟
      jedisPool.getResource().setex( telephone + RedisMessageConstant.SENDTYPE_ORDER,5 * 60*60,code.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
  }

  @RequestMapping("/send4Login")
 public Result validateCode4Login(String telephone){
      Integer code = ValidateCodeUtils.generateValidateCode(4);//生成验证码int 4位
     //调用发送短信
      try {
          SMSUtils.sendShortMessage(telephone,code.toString());
      } catch (ClientException e) {
          e.printStackTrace();//发送失败
          return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
      }
    //将生成的验证码缓存到redis,保存时间5分钟
      jedisPool.getResource().setex( telephone + RedisMessageConstant.SENDTYPE_LOGIN,5 * 60*60,code.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
  }


    @RequestMapping("/send4Register")
    public Result validateCode4Register(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(4);//生成验证码int 4位
        //调用发送短信
        try {
            SMSUtils.sendShortMessage(telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();//发送失败
            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将生成的验证码缓存到redis,保存时间5分钟
        jedisPool.getResource().setex( telephone + RedisMessageConstant.SENDTYPE_REGISTER,5 * 60*60,code.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
