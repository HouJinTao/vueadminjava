package com.imnu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imnu.entity.SysRole;
import com.imnu.mapper.SysRoleMapper;
import com.imnu.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-11-22
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> listRoleByUserId(Long id) {

        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + id));


        return sysRoles;
    }
}
