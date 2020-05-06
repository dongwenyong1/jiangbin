package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 11:13
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){

        try {
            PagesResult permissionList= permissionService.findPage(queryPageBean);
            return  new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            //查询数据失败
            return  new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }


    }


    @RequestMapping("/add")
    public  Result addPermission(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            //添加成功
            return  new Result(true,MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //添加权限失败
            return  new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
        }

    }

    @RequestMapping("/findById")
    public  Result findById(Integer id){
        try {
          Permission permission=  permissionService.findById(id);
          if (permission!=null){//查询权限成功
              return  new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
          }//未查到
            return  new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(false,MessageConstant.SYSTEME_RROR);
        }
    }

    @RequestMapping("/edit")//前端传来的是json字符串，用一个permission对象接收
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
                return  new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(false,MessageConstant.SYSTEME_RROR);
        }

    }


    @RequestMapping("/delete")//前端传来的是json字符串，用一个permission对象接收
    public Result delete(Integer id){
        try {
            permissionService.delete(id);
            return  new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(true,MessageConstant.DELETE_PERMISSION_FAIL);
        }

    }


    @RequestMapping("/findAll")
    public  Result findAll(){
        try {
           List<Permission> permission=  permissionService.findAll();
            if (permission!=null){//查询权限成功
                return  new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
            }//未查到
            return  new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(false,MessageConstant.SYSTEME_RROR);
        }
    }



}
