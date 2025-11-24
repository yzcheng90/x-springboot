package com.suke.czx.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suke.czx.modules.sys.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2016年9月18日 上午9:33:33
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询用户创建的角色ID列表
     */
    @Select("    select role_id\n" +
            "        from sys_role\n" +
            "        where create_user_id = #{createUserId}")
    List<Long> queryRoleIdList(String createUserId);

    /**
     * 查询用户拥有的角色ID列表
     */
    @Select("select role_id\n" +
            "        from sys_user_role\n" +
            "        where user_id = #{userId}")
    List<Long> queryUserRoleIdList(String userId);

    /**
     * 查询用户的所有菜单ID
     */
    @Select("SELECT DISTINCT rm.menu_id\n" +
            "        FROM sys_role_menu rm\n" +
            "                 LEFT JOIN sys_user_role ur ON ur.role_id = rm.role_id\n" +
            "        where ur.user_id = #{userId}")
    List<Long> queryAllMenuId(String userId);


    @Select("select t2.* from sys_user_role t1 LEFT JOIN sys_role t2 on t1.role_id = t2.role_id where t1.user_id = #{userId}")
    List<SysRole> getRoleListByUserId(@Param("userId") String userId);


}
