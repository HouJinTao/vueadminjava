package com.imnu.sercurity;

import cn.hutool.json.JSONUtil;
import com.imnu.common.lang.Result;
import com.imnu.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
/*        log.info("成功进入主页");
        //调整编码格式
       response.setContentType("application/json;charset=UTF-8");
        //用流的方式进行传输
        ServletOutputStream outputStream = response.getOutputStream();

        //生成jwt,并放置到请求头中
        //封装结果集
        Result result = Result.succ("");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        //流的任务结束
        outputStream.flush();
        outputStream.close();*/
    }

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("成功进入主页");
        //调整编码格式
        response.setContentType("application/json;charset=UTF-8");
        //用流的方式进行传输
        ServletOutputStream outputStream = response.getOutputStream();

        //生成jwt,并放置到请求头中
        String token = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(),token);
        //封装结果集
        Result result = Result.succ("");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        //流的任务结束
        outputStream.flush();
        outputStream.close();
    }
}
