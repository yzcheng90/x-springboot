package com.suke.czx.modules.msg.component;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.suke.czx.modules.msg.component.entity.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author czx
 * @title: SendMessageComponent
 * @projectName task-manage
 * @description: https://market.aliyun.com/apimarket/detail/cmapi029993
 * @date 2023/1/2917:08
 */
@Slf4j
@Component
public class AliyunNewSendMessageComponent extends SendMessageAbstract {

    @Override
    public boolean sendMessage(MessageBody messageBody) {
        String url = messageBody.getServiceUrl();
        String mobile = messageBody.getMobile();
        String code = messageBody.getCode();
        String appCode = messageBody.getAppCode();
        String message = messageBody.getTemplate().replaceAll("#code", code);

        String host = url + "?mobile=" + mobile + "&content=" + URLEncodeUtil.encode(message);
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
        try {
            HttpResponse response = HttpUtil
                    .createPost(host)
                    .headerMap(headers, true)
                    .execute();
            String body = response.body();
            JSONObject entries = JSONUtil.parseObj(body);
            String returnStatus = entries.getStr("ReturnStatus");
            if (StrUtil.isEmpty(returnStatus) || returnStatus.equals("Faild")) {
                this.sendMessageFail(messageBody, entries.getStr("Message"));
            } else {
                log.info("短信发送成功：{},任务ID：{}", message, entries.getStr("TaskID"));
                return true;
            }
        } catch (Exception e) {
            this.sendMessageFail(messageBody, e.getMessage());
        }
        return false;
    }


}
