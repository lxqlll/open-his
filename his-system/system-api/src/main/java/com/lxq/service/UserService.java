package com.lxq.service;

import com.lxq.domain.User;

/**
 * @describe 用户服务层
 * @author 1824992529
 * @time 2020年8月30日22点23分
 */
public interface UserService {
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return User 用户信息表
     */
    User queryUserByPhone(String phone);

    /**
     * 根据用户ID查询用户
     * @param userId 用户编号
     * @return User 用户信息表
     */
    User getOne(Long userId);

}
