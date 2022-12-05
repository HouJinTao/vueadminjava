package com.imnu.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imnu.common.lang.Const;
import com.imnu.common.lang.Result;
import com.imnu.entity.SysRole;
import com.imnu.entity.SysRoleMenu;
import com.imnu.entity.SysUserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-11-22
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController{

    @GetMapping("info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result info(@PathVariable Long id){

        SysRole sysRole = sysRoleService.getById(id);

        //获取角色相关联的菜单id
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());

        sysRole.setMenuIds(menuIds);
        return Result.succ(sysRole);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list(String name){

        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotBlank(name),SysRole::getName,name);

        Page<SysRole> pageData = sysRoleService.page(getPage(),lambdaQueryWrapper);

        return Result.succ(pageData);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@Validated @RequestBody SysRole sysRole){

        sysRole.setCreated(LocalDateTime.now());
        sysRole.setStatu(Const.STATUS_ON);

        sysRoleService.save(sysRole);

        return Result.succ(sysRole);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@Validated @RequestBody SysRole sysRole){

        sysRole.setUpdated(LocalDateTime.now());

        sysRoleService.updateById(sysRole);

        //更新缓存
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        return Result.succ(sysRole);
    }

    @PostMapping("/delete")
    @Transactional
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result delete(@RequestBody Long[] roleIds){
        //数组转集合
        sysRoleService.removeByIds(Arrays.asList(roleIds));

        //删除中间表
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id",roleIds));
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id",roleIds));

        //缓存同步删除
        Arrays.stream(roleIds).forEach(id ->{
            sysUserService.clearUserAuthorityInfoByRoleId(id);
        });
        return Result.succ("删除成功");
    }

    @PostMapping("/perm/{roleId}")
    @Transactional
    public Result list(@PathVariable("roleId") Long roleId,@RequestBody Long[] menuIds){

        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();

        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);

            sysRoleMenus.add(roleMenu);
        });

        //先删除原来的记录，再保存
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));

        sysRoleMenuService.saveBatch(sysRoleMenus);

        // 删除缓存
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.succ(menuIds);
    }
}

