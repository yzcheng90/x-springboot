package com.suke.czx.modules.msg.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.common.exception.RRException;
import com.suke.czx.common.utils.SpringContextUtils;
import com.suke.czx.modules.msg.component.SendMessage;
import com.suke.czx.modules.msg.component.entity.MessageBody;
import com.suke.czx.modules.msg.entity.SendMsg;
import com.suke.czx.modules.msg.entity.XMessageService;
import com.suke.czx.modules.msg.entity.XMessageServiceSendRecord;
import com.suke.czx.modules.msg.entity.XMessageServiceTemplate;
import com.suke.czx.modules.msg.mapper.XMessageServiceMapper;
import com.suke.czx.modules.msg.mapper.XMessageServiceSendRecordMapper;
import com.suke.czx.modules.msg.mapper.XMessageServiceTemplateMapper;
import com.suke.czx.modules.msg.service.XMessageServiceSendRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;


/**
 * 短信发送记录（按月分表）
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:40:53
 */
@Slf4j
@Service
@AllArgsConstructor
public class XMessageServiceSendRecordServiceImpl extends ServiceImpl<XMessageServiceSendRecordMapper, XMessageServiceSendRecord> implements XMessageServiceSendRecordService {

    public final XMessageServiceMapper xMessageServiceMapper;
    public final XMessageServiceTemplateMapper xMessageServiceTemplateMapper;

    @Override
    public boolean autoServiceSendMessage(SendMsg msg) {
        if (msg == null) {
            return false;
        }
        String mobile = msg.getMobile();
        // 正则表达式：匹配中国大陆11位手机号
        String telRegex = "^1" +
                "((3[0-9])|" +       // 13x-139
                "(4[014-9])|" +       // 14x
                "(5[0-35-9])|" +      // 15x (排除154)
                "(6[2567])|" +        // 16x (160/161已作他用)
                "(7[0-8])|" +         // 17x (179已作它用)
                "(8[0-9])|" +         // 18x
                "(9[0-35-9]))" +      // 19x
                "[0-9]{8}$";
        if (!Pattern.matches(telRegex, mobile)) {
            throw new RRException("手机号错误");
        }

        Integer tenancyId = msg.getTenancyId();
        if (tenancyId == null) {
            throw new RRException("租户错误");
        }

        XMessageService messageService;
        Integer serviceId = msg.getServiceId();
        // 根据服务查询，如果没有传服务，就查询当前租户下的默认服务
        if (serviceId != null) {
            messageService = xMessageServiceMapper.selectById(serviceId);
        } else {
            messageService = xMessageServiceMapper.selectOne(Wrappers
                    .<XMessageService>lambdaQuery()
                    .eq(XMessageService::getTenancyId, tenancyId)
                    .eq(XMessageService::getIsEnable, 1)
                    .eq(XMessageService::getIsDefault, 1)
                    .last("limit 1"));
        }
        if (messageService == null) {
            throw new RRException("短信服务未配置");
        }

        if (StrUtil.isNotEmpty(msg.getContent())) {
            // 设置模板
            messageService.setDefaultTemplate(msg.getContent());
        } else {
            // 如果有指定模板ID，就使用指定模板
            Integer templateId = msg.getTemplateId();
            if (templateId != null) {
                XMessageServiceTemplate serviceTemplate = xMessageServiceTemplateMapper.queryTemplate(templateId, serviceId);
                if (serviceTemplate != null) {
                    // 设置模板
                    messageService.setDefaultTemplate(serviceTemplate.getTemplateContent());
                }
            }
        }
        return this.sendMessage(messageService, msg);
    }

    @Override
    public boolean sendMessage(XMessageService messageService, SendMsg msg) {
        try {
            String serviceClass = messageService.getServiceClass();
            if (StrUtil.isEmpty(serviceClass)) {
                throw new RRException("短信服务配置错误");
            }
            // 使用反射选择对应服务类进行发送
            SendMessage sendMessage = (SendMessage) SpringContextUtils.getBean(Class.forName(messageService.getServiceClass()));

            // 发送短信
            String mobile = msg.getMobile();
            String code = msg.getCode();
            Integer tenancyId = msg.getTenancyId();
            MessageBody messageBody = new MessageBody();
            BeanUtil.copyProperties(messageService, messageBody);
            messageBody.setMobile(mobile);
            messageBody.setCode(code);
            messageBody.setTemplate(messageService.getDefaultTemplate());
            boolean bool = sendMessage.sendMessage(messageBody);
            // 保存短信发送记录
            String message = messageBody.getTemplate().replaceAll("#code", code);
            this.saveMessageRecord(mobile, message, bool, msg.getTemplateType(), tenancyId);
        } catch (Exception e) {
            log.error("发送失败,短信服务配置错误:{}", e.getMessage());
            throw new RRException("发送失败,短信服务配置错误");
        }
        return false;
    }

    /**
     * 保存短信发送记录
     */
    public void saveMessageRecord(String mobile, String message, boolean status, String type, Integer tenancyId) {
        XMessageServiceSendRecord record = new XMessageServiceSendRecord();
        record.setSendContent(message);
        record.setSendMobile(mobile);
        record.setSendType(type);
        Date date = new Date();
        record.setCreateTime(date);
        record.setRecordYear(DateUtil.year(date));
        record.setRecordMonth(DateUtil.month(date) + 1);
        record.setRecordDay(DateUtil.dayOfMonth(date));
        record.setSendStatus(status ? 1 : 0);
        record.setTenancyId(tenancyId);
        baseMapper.insert(record);
    }
}
