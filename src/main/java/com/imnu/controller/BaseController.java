package com.imnu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imnu.service.*;
import com.imnu.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    /**
     * 获取页码
     * @return
     */
    public Page getPage(){
        int current = ServletRequestUtils.getIntParameter(req,"current",1);
        int size = ServletRequestUtils.getIntParameter(req,"size",10);

        return new Page(current,size);
    }
}
