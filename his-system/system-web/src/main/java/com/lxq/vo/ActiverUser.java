package com.lxq.vo;

import com.lxq.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 存储用户/角色/权限类
 * @author 18249
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser implements Serializable {
    //用户信息
    private User user;
    //角色
    private List<String> roles = Collections.emptyList();
    //权限
    private List<String> permissions = Collections.emptyList();
}
