package com.imnu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imnu.common.dto.SysMenuDto;
import com.imnu.entity.SysMenu;
import com.imnu.entity.SysUser;
import com.imnu.mapper.SysMenuMapper;
import com.imnu.mapper.SysUserMapper;
import com.imnu.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imnu.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysUserService sysUserService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {

        //获取用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SysUser sysUser = sysUserService.getByUsername(username);

        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds);

        //转树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);
        //实体转DTO
        return convert(menuTree);
    }

    @Override
    public List<SysMenu> tree() {

        //获取所有菜单信息
        List<SysMenu> menus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));

        //转成树状结构
        return buildTreeMenu(menus);
    }

    private List<SysMenuDto> convert(List<SysMenu> menuTree) {

        List<SysMenuDto> sysMenuDtoList = new ArrayList<>();

        menuTree.forEach(m-> {

            SysMenuDto dto = new SysMenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());

            if (m.getChildren().size() >0){
                //子结点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            sysMenuDtoList.add(dto);
        });

            return sysMenuDtoList;
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {

        List<SysMenu> finalMenus = new ArrayList<>();

        //添加孩子
        for (SysMenu menu : menus) {

            for (SysMenu e : menus) {
                if (menu.getId() == e.getParentId()){
                    menu.getChildren().add(e);
                }
            }

            if (menu.getParentId() == 0L){
                finalMenus.add(menu);
            }
        }

        System.out.println(finalMenus.toString());

        //提出父节点

        return finalMenus;
    }
}
