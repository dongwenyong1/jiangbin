package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 15:40
 */
@Service(interfaceClass =MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Override
    public PagesResult findPage(QueryPageBean queryPageBean) {
        //初始化
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //调用通过条件查询list<Menu>的方法
      List<Menu> menuList= menuDao.findByCondition(queryPageBean.getQueryString());
      //处理集合
        PageInfo<Menu>   pageInfo=new PageInfo<>(menuList);

        return new  PagesResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void edit(Menu menu) {
      menuDao.edit(menu);

    }

    @Override
    public void delete(Integer id) {
     //先查询中间表是否有值，角色菜单表   t_role_menu
        long count = menuDao.findRoleCountByMenuId(id);//通过菜单id查询是与角色表关联
        if (count>0){
            //不能删除
            throw  new RuntimeException("不能删除");
        }else {
            menuDao.deleteById(id);

        }

    }

    @Override
    public List<Menu> findAll() {
        return menuDao.finAll();
    }
}
