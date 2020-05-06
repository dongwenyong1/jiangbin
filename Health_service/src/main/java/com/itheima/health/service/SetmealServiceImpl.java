package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.pojo.Setmeal;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @author ${dong}
 * @date 2020/1/3 0:26
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;


    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加的时候，需要考虑外键因素
        //关联套餐和检查组的关系
        setmealDao.add(setmeal);//添加数据时需要更新并获取最后的主键
        setCheckSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        //将图片名称保存到Redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

    }

    @Override
    public PagesResult findPage(Integer currentPage, Integer pageSize, String queryString) {
            //初始化
            PageHelper.startPage(currentPage, pageSize);
            List<Setmeal> setmealList = setmealDao.selectCountByCondition(queryString);
            // List<Setmeal> setmealList= setmealDao.selectCountByConditionlike(queryString);
          PageInfo  pageInfo = new PageInfo<>(setmealList);
        return new PagesResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Setmeal findSetmealById(Integer id) {

        return setmealDao.findSetmealById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBysetmealId(Integer id) {

        return setmealDao.findCheckGroupIdsBysetmealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {

        //先清除原来检查组和套餐的关系，根据setmeal.getid删除
        setmealDao.deleteAssociationWithCheckGroupBySid(setmeal.getId());
        //更新现有的检查组合套餐的关系
        setCheckSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        //更新数据库中套餐表的数据
        setmealDao.edit(setmeal);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

    }

    @Override
    public void deleteById(Integer id) {
        //先清除中间表的关系
        setmealDao.deleteAssociationWithCheckGroupBySid(id);
        //再执行删除

        setmealDao.deleteById(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findSetmealResultMapById (Integer id) {
        //查询套餐数据--基本信息+检查组（基本信息+检查项）
        //查询套餐基本信息+包含的检查组信息
        //查询检查组基本信息+包含的检查项
        //查询所有的检查项
            Setmeal setmeal= setmealDao.findSetmealResultMapById(id);

return setmeal;
    }

    @Override//返回的map要求<setmealCount（name1:100,name2:200） >  （setmealNames：(name1,name2)）
    public Map<String, Objects> getSetmealReport() {

        //查询套餐名称+数量 setmealCount（name1:100,name2:200）
        //查询套餐名称setmealNames(name1,name2)

        //思路：直接查询返回值为map(name,count),再进行处理即可
      List<String> setmealNames=new ArrayList<>();
      List<Map<String,Object>>   list=  setmealDao.getSetmealReport();
        for (Map<String, Object> map : list) {
            String name= (String) map.get("name");
            setmealNames.add(name);
        }
         Map resultmap=new HashMap();
          resultmap.put("setmealCount",list);//套餐名及预约数量map组成的list
          resultmap.put("setmealNames",setmealNames);//套餐名集
        return resultmap;
    }


    private  void setCheckSetmealAndCheckGroup(Integer steamId ,Integer[] checkGroupIds ){
        //遍历checkGroupIds数组，在数据库中建立关系
        if (checkGroupIds!=null&&checkGroupIds.length>0){
            for (Integer groupId : checkGroupIds) {
                setmealDao.setSetmealAndCheckGroup(steamId,groupId);
                //也可以封装成map集合进行添加
            }
        }

    }


}
