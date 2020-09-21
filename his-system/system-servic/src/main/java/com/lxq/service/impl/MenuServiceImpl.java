package com.lxq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxq.constants.Constants;
import com.lxq.domain.Menu;
import com.lxq.domian.SimpleUser;
import com.lxq.mapper.MenuMapper;
import com.lxq.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe 菜单权限服务实现类
 * @author 1824992529
 * @time 2020年8月30日
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService{

    /**
     * 声明MenuMapper对象
     */
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> selectMenuTree(boolean isAdmin,SimpleUser simpleUser) {
        //实例化创建list集合
        List<Menu>  enums = new ArrayList<>(20);
        //实例化创建QueryWrapper对象
        QueryWrapper query = new QueryWrapper();
        //添加条件
        query.eq(Menu.COL_STATUS, Constants.STATUS_TRUE);
        //添加范围
        query.in(Menu.COL_MENU_TYPE, new String[]{Constants.MENU_TYPE_C,Constants.MENU_TYPE_M,Constants.MENU_TYPE_F});
        //父节点排序
        query.orderByAsc(Menu.COL_PARENT_ID);
        if (isAdmin){
            //查询所有
            enums = menuMapper.selectList(query);
        }else {

            //ID查询
            enums = menuMapper.selectList(query);
        }
        return enums;
    }

}
