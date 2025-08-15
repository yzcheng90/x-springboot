package com.suke.czx.modules.tenancy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 渠道管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2024-11-29 14:48:31
 */
public interface TbPlatformTenancyMapper extends BaseMapper<TbPlatformTenancy> {

    @Select("select * from (select t1.*,t2.tenancy_name tenancyParentName from tb_platform_tenancy t1\n" +
            "left join tb_platform_tenancy t2\n" +
            "           on t1.tenancy_pid=t2.tenancy_id) t ${ew.customSqlSegment}")
    IPage<TbPlatformTenancy> pageList(IPage<TbPlatformTenancy> tbPlatformTenancyIPage, @Param("ew") QueryWrapper<TbPlatformTenancy> queryWrapper);

    @Select("select * from tb_platform_tenancy where is_default = 1 limit 1 ")
    TbPlatformTenancy getDefaultTenancy();
}
