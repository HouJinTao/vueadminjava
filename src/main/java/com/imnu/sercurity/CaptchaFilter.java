package com.imnu.sercurity;

import com.imnu.common.exception.CaptchaException;
import com.imnu.common.lang.Const;
import com.imnu.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURI();
        String str = "/login";
        if (str.equals(url) && httpServletRequest.getMethod().equals("POST")) {
            try {
                //校验验证码
                validate(httpServletRequest);
            }catch (CaptchaException e){
                //失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }



        }
        //如果不正确。就跳转到认证失败处理器
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
    // 校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) {

        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("token");

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(key)){
            throw new CaptchaException("验证码错误");
        }

        if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))) {
            throw new CaptchaException("验证码错误");
        }

        //一次性使用，用一次删一次
        redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}
