package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.enity.Result;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/2 9:11
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    //订阅service
    @Reference
    private CheckGroupService checkGroupService;

    //add.do
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();//添加失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //添加成功
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    //findPage.do
    @RequestMapping("/findPage")
    public PagesResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PagesResult pagesResult = checkGroupService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(), queryPageBean.getQueryString());

        return pagesResult;
    }

    @RequestMapping("/findCheckGroupById")
    public Result findCheckById(Integer id) {
        //
        CheckGroup checkGroup = checkGroupService.findCheckGroupById(id);
        if (checkGroup != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    //根据检查组的id查询所有的检查项目
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemByCheckGroupId(Integer id) {
        //
        List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return list;
    }

    //更新数据。
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        //调用service方法更新检查组
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();//更新失败
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkGroupService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();//删除失败
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        //删除成功
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
    @RequestMapping("/findAll")
    public  Result findAll(){

     List<CheckGroup> groupList= checkGroupService.findAll();
     if (groupList!=null&&groupList.size()>0)
     {
         return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,groupList);

     }
     return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

}