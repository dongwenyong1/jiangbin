package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/2 9:36
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Override
    public void add(CheckGroup checkGroup ,Integer[] checkitemIds) {
       checkGroupDao.add(checkGroup);
       //关联项目及检查组的表,设置检查组合检查项的关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public PagesResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //分页查询检查组

        //1完成初始化
        PageHelper.startPage(currentPage,pageSize);
       //2.查询list
        List<CheckGroup> list=checkGroupDao.selectByCondition(queryString);
        //后处理
        PageInfo<CheckGroup> pageInfo=new PageInfo<>(list);
        //传入list和总数并创建pageresult对象
        return new PagesResult(pageInfo.getTotal(),pageInfo.getList());
    }

    //查询检查组
    @Override
    public CheckGroup findCheckGroupById(Integer id) {
        return checkGroupDao.findCheckGroupById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
             //1.根据检查组的删除中间表，原有的关系
              checkGroupDao.deleteAssociation(checkGroup.getId());
              //调用方法：建立新表中检查组合检查项的关系
              setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
              //更新数据库数据
               checkGroupDao.edit(checkGroup);

    }

    @Override//删除检查组
    public void delete(Integer id) {
        //先要删除外键，再删除检查组
        checkGroupDao.deleteAssociation(id);
        checkGroupDao.deleteCheckGroup(id);

    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


    public  void setCheckGroupAndCheckItem( Integer checkGroupId,Integer[] checkitemIds ){
        //设置检查组合检查项的中间表
        if(checkitemIds!=null&&checkitemIds.length>0) {
            //遍历检查项的id数组,将数据传给checkdao进行存储
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.setCheckGroupAndCheckItem(checkGroupId,checkitemId);
                //也可以存一个集合。map(String ,Integer)=>("checkGroupId",checkGroupId) ,("checkitemId",checkitemId)
            }
        }
    }
}
