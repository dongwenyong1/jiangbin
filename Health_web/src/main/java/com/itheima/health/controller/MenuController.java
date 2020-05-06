package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 15:33
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
     @Reference
    private MenuService menuService;
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){

        try {
            PagesResult menuList= menuService.findPage(queryPageBean);
            return  new Result(true, MessageConstant.QUERY_MENUE_SUCCESS,menuList);
        } catch (Exception e) {
            e.printStackTrace();
            //查询数据失败
            return  new Result(false, MessageConstant.QUERY_MENUE_FAIL);
        }


    }


    @RequestMapping("/add")
    public  Result addPermission(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            //添加成功
            return  new Result(true,MessageConstant.ADD_MENUE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //添加权限失败
            return  new Result(false,MessageConstant.ADD_MENUE_FAIL);
        }

    }

    @RequestMapping("/findById")
    public  Result findById(Integer id){
        try {
            Menu menu=  menuService.findById(id);
            if (menu!=null){//查询权限成功
                return  new Result(true,MessageConstant.QUERY_MENUE_SUCCESS,menu);
            }//未查到
            return  new Result(false,MessageConstant.QUERY_MENUE_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(false,MessageConstant.SYSTEME_RROR);
        }
    }

    @RequestMapping("/edit")//前端传来的是json字符串，用一个permission对象接收
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
            return  new Result(true,MessageConstant.QUERY_MENUE_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(false,MessageConstant.SYSTEME_RROR);
        }

    }


    @RequestMapping("/delete")//前端传来的是json字符串，用一个permission对象接收
    public Result delete(Integer id){
        try {
            menuService.delete(id);
            return  new Result(true,MessageConstant.DELETE_MENUE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(true,MessageConstant.DELETE_MENUE_FAIL);
        }

    }

    @RequestMapping("/findAll")
    public  Result findAll(){
        try {
            List<Menu> menus=  menuService.findAll();
            if (menus!=null){//查询权限成功
                return  new Result(true,MessageConstant.QUERY_MENUE_SUCCESS,menus);
            }//未查到
            return  new Result(false,MessageConstant.QUERY_MENUE_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            return  new Result(false,MessageConstant.SYSTEME_RROR);
        }
    }





}
