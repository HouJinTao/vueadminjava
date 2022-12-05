package com.imnu.sercurity;

import cn.hutool.core.util.StrUtil;
import com.imnu.entity.SysUser;
import com.imnu.service.SysUserService;
import com.imnu.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("校验jwt");

        //获取登录凭证
        String jwt = request.getHeader(jwtUtils.getHeader());
        log.info(jwt+"----------------------------");
        //看看白名单里面有没有，有就放行
        if (StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null){
            throw new JwtException("token 异常");
        }

        //判断token是否过期
        if (jwtUtils.isTokenExpired(claim)){
            throw new JwtException("token已过期");
        }

        String username = claim.getSubject();
        //获取用户的权限信息

        //获取user信息
        SysUser user = sysUserService.getByUsername(username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,null,userDetailService.getUserAuthority(user.getId()));

        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request,response);
    }
}
