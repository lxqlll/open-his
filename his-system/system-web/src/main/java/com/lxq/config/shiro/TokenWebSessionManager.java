package com.lxq.config.shiro;

import com.lxq.constants.Constants;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.UUID;

/**
 * @Description 生成toke
 * 如果没有token生成一个返回到前台，
 * 如果有就从请求头里面取出来
 * @Author lxq
 */
@Configuration
public class TokenWebSessionManager extends DefaultWebSessionManager {

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //从头里面得到请求TOKEN 如果不存在就生成一个
        String header = WebUtils.toHttp(request).getHeader(Constants.TOKEN);
        if(StringUtils.hasText(header)){
            return  header;
        }
        return  UUID.randomUUID().toString() ;
    }
}
