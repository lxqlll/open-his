package com.lxq.controller.system;

import com.lxq.constants.Constants;
import com.lxq.constants.HttpStatus;
import com.lxq.domain.Menu;
import com.lxq.domian.SimpleUser;
import com.lxq.dto.LoginBodyDto;
import com.lxq.service.MenuService;
import com.lxq.vo.ActiverUser;
import com.lxq.vo.AjaxResult;
import com.lxq.vo.MenuTreeVo;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录
 * @author 18249
 */
@RestController
@Log4j2
@RequestMapping(value = "/login")
public class LoginController {

    /**
     * 声明UserService对象
     */
    @Autowired
    private MenuService menuService;

    /**
     * 用户登录
     * @param loginBodyDto 登陆的数据实体
     * @return AjaxResult 统一返回类型
     */
    @PostMapping(value = "/doLogin")
    public AjaxResult userLogin(@RequestBody @Validated LoginBodyDto loginBodyDto){
        try {
            //用户名
            String userName = loginBodyDto.getUsername();
            //密码
            String userPass =  loginBodyDto.getPassword();
            //判断是否为空
            if(null==userName) return new AjaxResult(HttpStatus.USER_OR_PASS_ERROR,"用户名不能为空");
            if(null==userPass) return new AjaxResult(HttpStatus.USER_OR_PASS_ERROR,"密码不能为空");
            //得到主体
            Subject subject = SecurityUtils.getSubject();
            //实例化创建UsernamePasswordToken对象
            AuthenticationToken authenticationToken = new UsernamePasswordToken(userName,userPass);
            //执行登录
            subject.login(authenticationToken);
            //获取唯一id
            Serializable token = subject.getSession().getId();
            Map<String,Serializable> tokenMap = new LinkedHashMap<>();
            tokenMap.put("token",token);
            return new AjaxResult(HttpStatus.SUCCESS,"登录成功",tokenMap);
        }catch (Exception e){
            log.error("userLogin"+e);
            return new AjaxResult(HttpStatus.ERROR,"登录失败,请检查用户名和密码");
        }
    }

    /**
     * 获取用户信息
     * @return AjaxResult 统一返回类型
     */
    @GetMapping(value = "/getUserInfo")
    public AjaxResult getUserInfo(){
        try{
            //得到主体
            Subject subject = SecurityUtils.getSubject();
            //获取ActiverUser对象
            ActiverUser activerUser = (ActiverUser)subject.getPrincipal();
            //实例化创建LinkedHashMap对象
            Map<String,Object> userMap = new LinkedHashMap<>(3);
            //添加数据
            userMap.put("username",activerUser.getUser().getUserName());
            userMap.put("portrait",activerUser.getUser().getPicture());
            userMap.put("roles",activerUser.getRoles());
            userMap.put("permissions",activerUser.getPermissions());
            return new AjaxResult(HttpStatus.SUCCESS,"获取用户信息成功",userMap);
        }catch(Exception e){
            log.error("getUserInfo"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }

    /**
     * 退出登录
     * @return AjaxResult 统一返回类型
     */
    @GetMapping(value = "/outLogin")
    public AjaxResult outLogin(){
        try{
            //得到主体
            Subject subject = SecurityUtils.getSubject();
            //退出登录
            subject.logout();
            return new AjaxResult(HttpStatus.SUCCESS,"退出登录成功");
        }catch(Exception e){
            log.error("outLogin"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
     }

    /**
     * 获取显示的菜单信息
     * @return AjaxResult 统一返回类型
     */
    @GetMapping(value = "/getMenus")
    public AjaxResult getMenu(){

        try {
            //得到主体
            Subject subject = SecurityUtils.getSubject();
            //获取ActiverUser对象
            ActiverUser activerUser = (ActiverUser)subject.getPrincipal();
            //是否超级管理员
            boolean isAdmin = activerUser.getUser().getUserType().equals(Constants.USER_ADMIN);
            //声明SimpleUser对象
            SimpleUser simpleUser = null;
            if (!isAdmin){
                //实例化创造SimpleUser对象
                simpleUser = new SimpleUser();
                simpleUser.setUserId(activerUser.getUser().getUserId());
                simpleUser.setUserName(activerUser.getUser().getUserName());
            }
            /**
             * 查询菜单
             */
            List<Menu> menuList = menuService.selectMenuTree(isAdmin,simpleUser);
            //创建一个空集合
            List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
            //构造菜单返回给前台
            menuList.forEach(item->{
                menuTreeVoList.add(new MenuTreeVo(item.getMenuId().toString(),item.getPath()));
            });
            return new AjaxResult(HttpStatus.SUCCESS,"获取显示的菜单信息成功",menuTreeVoList);
        }catch (Exception e){
            log.error("getMenu"+e);
            return new AjaxResult(HttpStatus.ERROR,"系统异常");
        }
    }
}
