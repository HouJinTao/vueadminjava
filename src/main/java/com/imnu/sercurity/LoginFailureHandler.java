package com.imnu.sercurity;

import cn.hutool.json.JSONUtil;
import com.imnu.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //调整编码格式
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //用流的方式进行传输
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        log.info("验证失败");
        //封装结果集
        Result result = Result.fail(e.getMessage());

        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        //流的任务结束
        outputStream.flush();
        outputStream.close();
    }
}
