package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/4 21:44
 */
@RestController
@RequestMapping("/setmeal")
public class MobileSetmealController {
    @Reference
    private SetmealService setmealService;
     @Autowired
    JedisPool jedisPool;
    //获取setmeal的方法,从数据库中获取套餐列表

    Gson gson= new Gson();
    @RequestMapping("/getSetmeal")
   public Result getSetmeal(){
        List<Setmeal> setmealList =null;
        try {
            String s = jedisPool.getResource().get(RedisConstant.SETMEAL_LIST_RESOURCES);
            if (s==null||"".equals(s)){
                setmealList = setmealService.findAll();
                 s = gson.toJson(setmealList);//存到redis中
                jedisPool.getResource().set(RedisConstant.SETMEAL_LIST_RESOURCES,s);
            }
            setmealList=gson.fromJson(s,new TypeToken<List<Setmeal>>(){}.getType());
            //查询套餐成功
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            //查询套餐失败
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


   /* @RequestMapping("/findById")
  public Result findSetmealResultMapById(Integer id){
        Setmeal setmeal=null;
        try {
            String s = jedisPool.getResource().get(RedisConstant.SETMEAL_DETAIL_RESOURCES);
            if (s==null||"".equals(s)){
                setmeal= setmealService.findSetmealResultMapById(id);
                s = gson.toJson(setmeal);
                jedisPool.getResource().set(RedisConstant.SETMEAL_DETAIL_RESOURCES,s);
            }
             setmeal = gson.fromJson(s, Setmeal.class);
            //查询套餐成功
            return  new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            //查询套餐失败
            return  new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }*/



    @RequestMapping("/findById")
  public Result findSetmealResultMapById(Integer id){
        Setmeal setmeal=null;
        List<Setmeal> setmealList=new ArrayList<>();
        try {//获取list集合，遍历list集合
            String s = jedisPool.getResource().get(RedisConstant.SETMEAL_DETAIL_LIST_RESOURCES);
            if (s==null||"".equals(s)){
                setmeal= setmealService.findSetmealResultMapById(id);

               setmealList.add(setmeal);

            }else  {
                setmealList=gson.fromJson(s,new TypeToken<List<Setmeal>>(){}.getType());
                boolean flag=false;
                for (Setmeal setmeal1 : setmealList) {
                    if (setmeal1.getId()==id) {
                        //包含
                        flag=true;
                        //设置该值为所需要的值
                        setmeal=setmeal1;
                    }
                }

                //防止更新数据
                if (!flag){//没有值 调用方法获取setmeal，并存到list中
                    setmeal=setmealService.findSetmealResultMapById(id);
                  setmealList.add(setmeal);
                }
            }

            s = gson.toJson(setmealList);
            jedisPool.getResource().set(RedisConstant.SETMEAL_DETAIL_RESOURCES,s);

            //查询套餐成功
            return  new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            //查询套餐失败
            return  new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
