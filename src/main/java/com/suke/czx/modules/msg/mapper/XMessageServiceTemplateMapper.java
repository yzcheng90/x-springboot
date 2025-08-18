package com.suke.czx.modules.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suke.czx.modules.msg.entity.XMessageServiceTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 短信模板
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:48:07
 */
public interface XMessageServiceTemplateMapper extends BaseMapper<XMessageServiceTemplate> {


    @Select("select t2.* from x_message_service_template_config t1,x_message_service_template t2 " +
            "where " +
            "t1.template_id = t2.template_id " +
            "and t1.template_id = #{templateId} " +
            "and t1.service_id = #{serviceId} and t2.is_enable = 1 limit 1;")
    XMessageServiceTemplate queryTemplate(@Param("templateId") Integer templateId,@Param("serviceId") Integer serviceId);

}
