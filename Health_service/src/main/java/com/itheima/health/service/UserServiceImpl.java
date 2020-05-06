package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.enity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ${dong}
 * @date 2020/1/6 16:04
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
     private MenuDao menuDao;

    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
    @Override
    public User findUserByUsername(String username) {
        //查询用户信息
        User user = userDao.findUserByUsername(username);

        return user;
    }

    @Override
    public PagesResult findPage(QueryPageBean queryPageBean) {
        //初始化
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //通过条件查询列表
         List<User> userList =  userDao.findPage(queryPageBean.getQueryString());
        PageInfo<User> pageInfo=new PageInfo<>(userList);
        return new PagesResult(pageInfo.getTotal(),pageInfo.getList());//设置totol和rows
    }

    @Override
    public void add(User user, Integer[] roleIds) {

        //加密用户密码
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //增加用户的相关信息
        userDao.add(user);

        //设置中间表   用户-角色
        setUserAssociationWithRole(user.getId(),roleIds);
    }




    @Override
    public void edit(User user, Integer[] roleIds) {
       //清除中间表的关系
        removeUserAssociationWithRoleByUid(user.getId());
        // 设置中间表
        setUserAssociationWithRole(user.getId(),roleIds);
        //更新用户表

        userDao.edit(user);
    }


    @Override
    public User findUserById(Integer id) {
        return userDao.findUserById(id);
    }

    @Override
    public void delete(Integer id) {
         //查询中间表是否有数据

        long count=userDao.findUserRoleCountByUid(id);
        if (count>0){
            throw  new RuntimeException("该用户包含角色，无法删除，如果想删除，请先删除用户的角色信息");

        }else {
            userDao.delete(id);

        }


    }

    @Override
    public Result getUserMenusByUsername(String username) {
        //通过用户名查询用户 id ,再查询用户角色   根据用户角色查询菜单
          User user= userDao.findUserByUsername(username);
          List<Integer> menuIdsByRoleId = roleDao.findMenuIdsByRolePid(user.getId());//查询父类id再查询子类id
          List<Integer> menuIdsByAllRoleId = roleDao.findMenuIdsByRoleId(user.getId());//查询父类id再查询子类id
          //遍历ids获取菜单详情：思路：获取菜单的父类id  ，不为null时，再通过
        List<Object> resultList=new ArrayList<>();//数据集合
        for (Integer pid : menuIdsByRoleId) {
            Menu menu = menuDao.findById(pid);
            Map<String,Object>  map=new HashMap<>();//代表一个大菜单所有信息
            List<Map<String,Object>> childList=new ArrayList<>();
            map.put("path",menu.getPath());
            map.put("title",menu.getName());
            map.put("icon",menu.getIcon());
            map.put("linkUrl",menu.getLinkUrl());

            for (Integer cid : menuIdsByAllRoleId) {
                //获取字集合，传入父类id和子类id
                Menu menu1=menuDao.findByPidAndCid(pid,cid);
                if (menu1!=null){
                    Map<String,Object>  map1=new HashMap<>();//创建子菜单
                    map1.put("path",menu1.getPath());
                    map1.put("title",menu1.getName());
                    map1.put("icon",menu1.getIcon());
                    map1.put("linkUrl",menu1.getLinkUrl());
                  childList.add(map1);
                }
                map.put("children",childList);//添加子列表
            }

        resultList.add(map);
        }

        return new Result(true, MessageConstant.QUERY_MENUE_SUCCESS,resultList) ;
    }

    @Override
    public User register(Map map) {
        userDao.register(map);
        return userDao.findUserByTelephone((String)map.get("telephone"));
    }

    private void removeUserAssociationWithRoleByUid(Integer id) {
        userDao.removeUserAssociationWithRoleByUid(id);

    }
    private void setUserAssociationWithRole(Integer id, Integer[] roleIds) {

        if (roleIds!=null&&roleIds.length>0){
            for (Integer roleId : roleIds) {
               userDao.setUserAssociationWithRole(id,roleId);
            }
        }
    }

}
