package com.imnu.sercurity;

import cn.hutool.json.JSONUtil;
import com.imnu.common.lang.Result;
import com.imnu.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtLoginoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (authentication != null){
            new SecurityContextLogoutHandler().logout(httpServletRequest, response, authentication);
        }


        //调整编码格式
        response.setContentType("application/json;charset=UTF-8");
        //用流的方式进行传输
        ServletOutputStream outputStream = response.getOutputStream();

        response.setHeader(jwtUtils.getHeader(),"");
        //封装结果集
        Result result = Result.succ("");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        //流的任务结束
        outputStream.flush();
        outputStream.close();
    }
}
