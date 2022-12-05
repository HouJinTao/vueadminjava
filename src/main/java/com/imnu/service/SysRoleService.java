package com.imnu.service;

import com.imnu.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-11-22
 */
public interface SysRoleService extends IService<SysRole> {

    List<SysRole> listRoleByUserId(Long id);
}
