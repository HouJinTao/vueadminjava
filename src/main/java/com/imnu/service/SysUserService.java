package com.imnu.service;

import com.imnu.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-11-22
 */
public interface SysUserService extends IService<SysUser> {
    //根据用户名获取用户信息
    SysUser getByUsername(String s);

    String getUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfo(String username);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void clearUserAuthorityInfoByMenuId(Long menuId);
}
