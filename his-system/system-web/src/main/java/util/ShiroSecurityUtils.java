package util;

import com.lxq.domain.User;
import com.lxq.domian.SimpleUser;
import com.lxq.vo.ActiverUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.List;


/**
 * shiro工具类
 * @author  lxq
 * @time 2020年9月23日 09:56:06
 */
public class ShiroSecurityUtils {

    /**
     * 得到当前登录用户的对象ActiverUser
     * @return ActiverUser 存储用户/角色/权限类
     */
    public static ActiverUser getCurrentActiverUser(){
        //得到主体
        Subject subject = SecurityUtils.getSubject();
        //获取Principal
        ActiverUser activerUser = (ActiverUser)subject.getPrincipal();

        return activerUser;
    }


    /**
     * 得到当前登录用户的对象User
     * @return User 用户信息表
     */
    public static User getCurrentUser(){
        return getCurrentActiverUser().getUser();
    }

    /**
     * 得到当前登录用户的对象User的SimpleUser
     * @return SimpleUser
     */
    public static SimpleUser getCurrentSimpleUser(){
        User user = getCurrentUser();
        return new SimpleUser(user.getUserId(),user.getUserName());
    }

    /**
     * 得到当前登录用户的对象的用户名
     * @return String 用户名
     */
    public static String getCurrentUserName(){
        return getCurrentUser().getUserName();
    }

    /**
     * 得到当前登录用户的对象的用户角色
     * @return List
     */
    public static List<String> getCurrentUserRole(){
        return getCurrentActiverUser().getRoles();
    }

    /**
     * 得到当前登录用户的对象的用户权限
     * @return List
     */
    public static List<String> getCurrentUserPermissions(){
        return getCurrentActiverUser().getPermissions();
    }
}
