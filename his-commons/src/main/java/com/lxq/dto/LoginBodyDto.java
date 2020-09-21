package com.lxq.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @describe 登陆的数据输出对象
 * @author 18249992529
 * @time 2020年8月29日
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBodyDto {
    //  用户名
    @NotNull(message = "用户名不能为空")
    private String username;
    //  密码
    @NotNull(message = "密码不能为空")
    private String password;
    // 验证码
    private String code;
}
