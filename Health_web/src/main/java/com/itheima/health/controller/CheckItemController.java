package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ${dong}
 * @date 2019/12/31 16:54
 */
/*检查项目控制器*/
@RequestMapping("/checkitem")
@RestController
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    @RequestMapping("/add.do")//新增检查项
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result addCheckItems(@RequestBody CheckItem checkItem) {

        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {//出现异常，添加检查项失败

            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/findPage.do")//用一个查询bean来接收
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    public PagesResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PagesResult pagesResult = checkItemService.findPage(
                queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
           return pagesResult;
    }

    @RequestMapping("/delete.do")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验，使用CHECKITEM_DELETE123测试
    public Result deleteById(Integer id){
        try {
            checkItemService.deleteById(id);//删除成功

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findItemById.do")
    public Result findItemById(Integer id){

        CheckItem checkItem= checkItemService.findItemById(id);//根据id查询记录
          if (checkItem!=null){
              return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
          }else {

            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);}
    }

    @RequestMapping("/edit.do")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限校验
    public Result updateItem(@RequestBody CheckItem checkItem){

        try {
            checkItemService.update(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
     return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);

    }



    @RequestMapping("/findAll.do")//查询所有的检查项目
    public  Result findAll(){
        List<CheckItem> list =checkItemService.findAll();
        if (list!=null&&list.size()>0){//有数据查询成功，无数据查询失败
            return  new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }
       return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }

}
