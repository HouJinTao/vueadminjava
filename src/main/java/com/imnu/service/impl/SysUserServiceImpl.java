package com.imnu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imnu.entity.SysMenu;
import com.imnu.entity.SysRole;
import com.imnu.entity.SysUser;
import com.imnu.entity.SysUserRole;
import com.imnu.mapper.SysUserMapper;
import com.imnu.service.SysMenuService;
import com.imnu.service.SysRoleService;
import com.imnu.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imnu.util.RedisUtil;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-11-22
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleService sysRoleService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private RedisUtil redisUtil;



    @Override
    public SysUser getByUsername(String s) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername,s);
        SysUser sysUser = this.getOne(lambdaQueryWrapper);
        return sysUser;
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {

        SysUser sysUser = sysUserMapper.selectById(userId);

        List<Integer> integerList;
        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())){
            authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
        }else {
            //获取角色
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from sys_user_role where user_id = " + userId));
            if (roles.size() > 0){
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            //获取菜单操作编码

            List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
            if (menuIds.size() > 0 ){

                List<SysMenu> sysMenus = sysMenuService.listByIds(menuIds);

                String menuPerms = sysMenus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));

                authority = authority.concat(menuPerms);
            }

            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(),authority,60*60);

        }


        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>()
                .inSql("id", "select user_id from sys_user_role where role_id = " + roleId));

        sysUsers.forEach(u ->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {

        List<SysUser> sysUsers = sysUserMapper.listByMenuId(menuId);

        sysUsers.forEach(u ->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
}
