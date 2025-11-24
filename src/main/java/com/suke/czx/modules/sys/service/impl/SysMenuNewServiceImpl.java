package com.suke.czx.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.UserUtil;
import com.suke.czx.modules.sys.entity.SysMenuNew;
import com.suke.czx.modules.sys.mapper.SysMenuNewMapper;
import com.suke.czx.modules.sys.mapper.SysRoleMapper;
import com.suke.czx.modules.sys.service.SysMenuNewService;
import com.suke.czx.modules.sys.vo.RouterMetaVO;
import com.suke.czx.modules.sys.vo.SysMenuNewVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class SysMenuNewServiceImpl extends ServiceImpl<SysMenuNewMapper, SysMenuNew> implements SysMenuNewService {

    private final SysMenuNewMapper sysMenuNewMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    public List<SysMenuNewVO> getUserMenu() {
        return getRouterChildList(0l);
    }

    public List<SysMenuNewVO> getRouterChildList(Long menuId) {
        List<SysMenuNewVO> routerEntities = new ArrayList<>();
        QueryWrapper<SysMenuNew> queryWrapper = new QueryWrapper<>();
        final String userId = UserUtil.getUserId();
        if (!userId.equals(Constant.SUPER_ADMIN)) {
            // 用户配置的菜单
            final List<Long> menuIds = sysRoleMapper.queryAllMenuId(userId);
            if (CollUtil.isNotEmpty(menuIds)) {
                queryWrapper.lambda().in(SysMenuNew::getMenuId, menuIds);
            } else {
                return routerEntities;
            }
        }

        if (menuId != null) {
            queryWrapper.lambda().eq(SysMenuNew::getParentId, menuId);
        }
        queryWrapper.lambda().eq(SysMenuNew::isDisabled, true);
        queryWrapper.lambda().orderByAsc(SysMenuNew::getOrderSort);
        List<SysMenuNew> sysMenus = sysMenuNewMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(sysMenus)) {
            sysMenus.forEach(sysMenu -> {
                SysMenuNewVO entity = new SysMenuNewVO();
                entity.setMenuId(sysMenu.getMenuId());
                entity.setParentId(sysMenu.getParentId());
                entity.setPath(sysMenu.getPath());
                entity.setName(sysMenu.getName());
                entity.setComponent(sysMenu.getComponent());
                entity.setRedirect(sysMenu.getRedirect());
                entity.setTitle(sysMenu.getTitle());
                entity.setOrderSort(sysMenu.getOrderSort());
                // 设置mate
                RouterMetaVO metaVO = new RouterMetaVO();
                metaVO.setTitle(sysMenu.getTitle());
                metaVO.setIsLink(sysMenu.getIsLink());
                metaVO.setHide(sysMenu.isHide());
                metaVO.setKeepAlive(sysMenu.isKeepAlive());
                metaVO.setAffix(sysMenu.isAffix());
                metaVO.setIframe(sysMenu.isIframe());
                metaVO.setIcon(sysMenu.getIcon());
                metaVO.setRoles(sysMenu.getRoles());
                entity.setMeta(metaVO);
                // 查询子菜单
                entity.setChildren(this.getRouterChildList(sysMenu.getMenuId()));
                routerEntities.add(entity);
            });
        }
        return routerEntities;
    }

}
