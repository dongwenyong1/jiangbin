package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());

        }catch (Exception e){

            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }


    @RequestMapping("/getUserMenus")
    public Result getUserMenu()throws Exception{
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUserMenusByUsername(user.getUsername());
        }catch (Exception e){

            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }




    @RequestMapping("/findPage")
    public PagesResult findPage(@RequestBody QueryPageBean queryPageBean){
            return  userService.findPage(queryPageBean);
        }



    @RequestMapping("/add")
    public  Result add(@RequestBody User user,Integer [] roleIds){

        try {
            userService.add(user,roleIds);
            return new Result(true,MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_USER_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody User user,Integer [] roleIds){

        try {
            userService.edit(user,roleIds);
            return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
    }

    @RequestMapping("/findUserById")
    public  Result findUserById(Integer id){
              User user=   userService.findUserById(id);
              if (user!=null){
                  return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
              }else {
        return new Result(false,MessageConstant.QUERY_USER_FAIL);}
    }

    @RequestMapping("/delete")
    public  Result delete(Integer id){
        try {
            userService.delete(id);
            return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
        }catch (RuntimeException e) {
            e.printStackTrace();
            return  new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_USER_FAIL);}
        }


    }


