package com.suke.czx.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suke.czx.modules.sys.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 接口权限管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-05-17 14:48:21
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    @Select("SELECT * from sys_permission where permission_id in (SELECT t1.permission_id from sys_role_permission t1,sys_user_role t2 where t1.role_id = t2.role_id and t2.user_id = #{userId})")
    List<SysPermission> getPermissionListByUserId(@Param("userId") String userId);


    @Select("select * from (SELECT t1.*,t2.title as menuName from sys_permission t1 left join sys_menu_new t2 on t1.menu_id = t2.menu_id ) t ")
    List<SysPermission> getAllList();

}
