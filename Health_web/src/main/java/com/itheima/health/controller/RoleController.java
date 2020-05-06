package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 17:29
 */
@RequestMapping("/role")
@RestController
public class RoleController {
    @Reference
    private RoleService roleService;

    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            PagesResult roleList= roleService.findPage(queryPageBean);
            return  new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,roleList);
        } catch (Exception e) {
            e.printStackTrace();
            //查询数据失败
            return  new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }


    @RequestMapping("/add")
    public Result add( @RequestParam("menuIds") Integer[] menuIds,@RequestParam("permissionIds") Integer[] permissionIds,@RequestBody Role role){

        try {
            roleService.add(menuIds,permissionIds,role);
            return  new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //添加失败
            return  new Result(false,MessageConstant.ADD_ROLE_FAIL);
        }


    }

    @RequestMapping("/findRoleById")
    public Result findRoleById(Integer id){

        try {
           Role role= roleService.findRoleById(id);
            return  new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,role);
        } catch (Exception e) {
            e.printStackTrace();
            //添加失败
            return  new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }


    }



    @RequestMapping("/findMenuIdsByRoleId")
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        List<Integer> list = roleService.findMenuIdsByRoleId(id);
        return list;
    }



    @RequestMapping("/findPermisssionIdsByRoleId")
    public List<Integer> findPermisssionIdsByRoleId(Integer id) {
        List<Integer> list = roleService.findPermisssionIdsByRoleId(id);
        return list;
    }



    @RequestMapping("/edit")
    public Result edit( @RequestParam("menuIds") Integer[] menuIds,@RequestParam("permissionIds") Integer[] permissionIds,@RequestBody Role role){

        try {
            roleService.edit(menuIds,permissionIds,role);
            return  new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
        }
        catch (Exception e) {
            e.printStackTrace();
            //添加失败
            return  new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }


    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            roleService.deleteByRoleId(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (RuntimeException e){
            return  new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            //添加失败
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }


    @RequestMapping("/findAll")
    public  Result findAll(){

        List<Role> roleList= roleService.findAll();
        if (roleList!=null&&roleList.size()>0)
        {
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,roleList);

        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }


}
