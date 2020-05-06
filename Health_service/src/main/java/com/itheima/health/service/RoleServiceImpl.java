package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.enity.PagesResult;
import com.itheima.health.enity.QueryPageBean;
import com.itheima.health.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ${dong}
 * @date 2020/1/9 17:34
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public PagesResult findPage(QueryPageBean queryPageBean) {
       //初始化pagehelp
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //通过条件查询列表
        List<Role> roleList=roleDao.findRolesByCondition(queryPageBean.getQueryString());
       //new PageInform
        PageInfo<Role> pageInfo=new PageInfo<>(roleList);

        return new PagesResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void add(Integer[] menuIds, Integer[] permissionIds, Role role) {
        //1.增加t_role表中的基本数据
        roleDao.add(role);
        //2.设置权限和角色中间表的关系
        setRoleAndPermission(role.getId(),permissionIds);
        //3.设置菜单表和角色中间表的关系
        setRoleAndMenu(role.getId(),menuIds);
    }

    @Override
    public List<Integer> findMenuIdsByRoleId(Integer id) {

        return roleDao.findMenuIdsByRoleId(id);
    }

    @Override
    public List<Integer> findPermisssionIdsByRoleId(Integer id) {
        return roleDao.findPermisssionIdsByRoleId(id);
    }

    @Override
    public void edit(Integer[] menuIds, Integer[] permissionIds, Role role) {
        //先通过角色id删除中间表信息，
        removeRoleAssociation2Menu(role.getId());
        removeRoleAssociation2permission(role.getId());
        //设置中间表的数据
        setRoleAndMenu(role.getId(),menuIds);
        setRoleAndPermission(role.getId(),permissionIds);
        //更新角色信息
        roleDao.edit(role);
    }

    @Override
    public void deleteByRoleId(Integer id) {
        //查询中间表的数据,菜单-角色      权限-角色
        List<Integer> menuIds = roleDao.findMenuIdsByRoleId(id);
        List<Integer> permisssionIds = roleDao.findPermisssionIdsByRoleId(id);
        if (menuIds!=null&&menuIds.size()>0){
            //提示该角色包含菜单表，不能删除
            throw new RuntimeException("角色包含菜单表，不能删除，请先删除角色对应的菜单表");
        }
        if (permisssionIds!=null&&permisssionIds.size()>0){

            throw new RuntimeException("角色包含权限表，不能删除,请先删除角色对应的权限");
        }
        roleDao.delete(id);
    }

    @Override
    public Role findRoleById(Integer id) {
       return roleDao.indRoleById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    //可以直接删除的方法
    public void deleteByRoleId2(Integer id) {
        //查询中间表的数据,菜单-角色      权限-角色 删除中间表的值
       removeRoleAssociation2Menu(id);
       removeRoleAssociation2permission(id);
       roleDao.delete(id);
    }

    private void removeRoleAssociation2permission(Integer id) {
        roleDao.removeRoleAssociation2permission(id);

    }


    private void removeRoleAssociation2Menu(Integer id) {
        roleDao.removeRoleAssociation2Menu(id);
    }

    private void setRoleAndMenu(Integer rid, Integer[] menuIds) {
        //遍历数组并添加中间表
        for (Integer menuId : menuIds) {
            roleDao.setRoleMenu(menuId,rid);
        }

    }

    private void setRoleAndPermission(Integer rid, Integer[] permissionIds) {
        //遍历数据添加中间表
        for (Integer permissionId : permissionIds) {
            roleDao.setRolePermission(permissionId,rid);

        }
    }


}
