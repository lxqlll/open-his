package com.lxq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxq.domain.Menu;
import com.lxq.domian.SimpleUser;

import java.util.List;

/**
 * @describe 菜单权限服务层
 * @author 1824992529
 * @time 2020年8月30日
 */
public interface MenuService extends IService<Menu>{


    /**
     * 查询菜单信息,如果查用户是超级管理员,那么查询所有菜单和权限,如果用户是普通用户,那么根据用户ID关联角色和权限
     * @param isAdmin 是否为超级管理员
     * @param simpleUser 登录用户对象
     * @return
     */
    public List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser);

}
