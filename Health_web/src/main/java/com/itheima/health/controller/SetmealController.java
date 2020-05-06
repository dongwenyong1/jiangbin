package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * @author ${dong}
 * @date 2020/1/3 0:06
 */
@RequestMapping("/setmeal")
@RestController
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

    //上传图片
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            //将图片存在redis中，基于redis的set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        try {
            setmealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();//添加失败
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PagesResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PagesResult pagesResult = setmealService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pagesResult;
    }

    @RequestMapping("/findSetmealById")//通过id查询套餐信息
    public Result findSetmealById(Integer id) {
        Setmeal setmeal = setmealService.findSetmealById(id);
        if (setmeal == null) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        } else {

            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        }
    }

    @RequestMapping("/findCheckGroupIdsBysetmealId")//查询套餐包含的检查组，list<>
    public List<Integer> findCheckGroupIdsBysetmealId(Integer id) {
        List<Integer> checkGroupIds = setmealService.findCheckGroupIdsBysetmealId(id);
        return checkGroupIds;
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        try {
            setmealService.edit(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();//添加失败
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

   @RequestMapping("/deleteById")
    public Result deleteById(Integer id){

       try {
          setmealService.deleteById(id);
       } catch (Exception e) {//删除失败
           e.printStackTrace();
           return  new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
       }
return  new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
   }


}