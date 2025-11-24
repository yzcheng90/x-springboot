package com.suke.czx.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suke.czx.modules.sys.entity.SysRole;

import java.util.List;


/**
 * 角色
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2016年9月18日 上午9:42:52
 */
public interface SysRoleService extends IService<SysRole> {
    List<SysRole> getRoleListByUserId(String userId);

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(String createUserId);

    List<Long> queryUserRoleIdList(String userId);

    void deleteBath(Long id);

    void saveRoleMenu(SysRole role);

    void updateRoleMenu(SysRole role);

}
