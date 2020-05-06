package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.enity.PagesResult;
import net.sf.jsqlparser.statement.alter.Alter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/1 0:39
 */
@Service(interfaceClass = CheckItemService.class)//指定接口，供创建事务
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
   @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
   checkItemDao.add(checkItem);
    }



    @Override
    public PagesResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //采用mybatiss的分页插件
        //1.完成对分页初始化工作
        PageHelper.startPage(currentPage,pageSize);
        //2.查询
        List<CheckItem> list=checkItemDao.selectBycondiction(queryString);
        //3.后处理：pageHelper会根据查询的结果再封装成pageHelper对应的实体类
        PageInfo<CheckItem> pageInfo=new PageInfo<>(list);
        //将总页数及list传给pageinfo并创建对象返回即可


        return new PagesResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void deleteById(Integer id) {
        //先要判断检查项是否关联检查组

        long count=checkItemDao.findCountBycheckItemId(id);
         if (count>0){
             //已经检查项已经被引用，不能删除
             throw  new RuntimeException(MessageConstant.CHECKITEM_ISCONNENCT );
         }

        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findItemById(Integer id) {
        return checkItemDao.findItemById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {

        return checkItemDao.findAll();
    }
}
