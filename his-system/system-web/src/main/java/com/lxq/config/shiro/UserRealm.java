package com.lxq.config.shiro;

import com.lxq.domain.User;
import com.lxq.service.UserService;
import com.lxq.vo.ActiverUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * 自定义realm
 * @author 18249
 */
public class UserRealm extends AuthorizingRealm {

    /**
     * 声明UserService对象
     */
    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //得到ActiverUser对象
        ActiverUser activerUser = (ActiverUser)principalCollection.getPrimaryPrincipal();
        //实例化创建AuthorizationInfo对象
        AuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    /**
     * 授权
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //拿到用户名
        String userName = (String)authenticationToken.getPrincipal();
        //查询用户
        User user = userService.queryUserByPhone(userName);
        if(null == user){
            return null;
        }else{

            ActiverUser activerUser = new ActiverUser(user,null,null);

            //实例化创建SimpleAuthenticationInfo对象
            AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(activerUser,
                                                                                 user.getPassword(),
                                                                                 ByteSource.Util.bytes(user.getSalt()),
                                                                                 this.getName());
            return authenticationInfo;
        }
    }
}
