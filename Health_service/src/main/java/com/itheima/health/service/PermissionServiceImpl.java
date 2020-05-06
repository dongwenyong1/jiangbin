package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 11:27
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public PagesResult findPage(QueryPageBean queryPageBean) {
        //初始化
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //查询list
       List<Permission>  permissionList=  permissionDao.findByCondiction(queryPageBean.getQueryString());

        PageInfo<Permission> pageInfo=new PageInfo<>(permissionList);

        return new PagesResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    @Override//通过权限id权限信息
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public void delete(Integer id) {
        //首先需要判断是否有外键关联：t_role_permission
      Long count=  permissionDao.findAssociationCountWithRole(id);

      if (count>0){
          //有关联，无法删除
          throw  new RuntimeException();
      }else { permissionDao.deleteById(id);}
      //可以删除

    }

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }
}
