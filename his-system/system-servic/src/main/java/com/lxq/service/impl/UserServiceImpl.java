package com.lxq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxq.domain.User;
import com.lxq.mapper.UserMapper;
import com.lxq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @describe 用户服务层实现类
 * @author 1824992529
 * @time 2020年8月30日22点23分
 */
@Service
public class UserServiceImpl  implements UserService{

    /**
     * 声明UserMapper对象
     */
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByPhone(String phone) {
        //实例化创建QueryWrapper对象
        QueryWrapper query = new QueryWrapper();
        //添加查询条件
        query.eq(User.COL_PHONE,phone);
        return userMapper.selectOne(query);
    }

    @Override
    public User getOne(Long userId) {
        return userMapper.selectById(userId);
    }
}
