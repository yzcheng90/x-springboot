package com.suke.czx.modules.msg.api;

import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.application.annotation.ApplicationAuth;
import com.suke.czx.modules.msg.entity.SendMsg;
import com.suke.czx.modules.msg.service.XMessageServiceSendRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 短信服务
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:51:27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/msg/service")
@Tag(name = "XMessageServiceController", description = "短信服务")
public class ApiXMessageServiceController extends AbstractController {

    private final XMessageServiceSendRecordService xMessageServiceSendRecordService;

    @ApplicationAuth
    @PostMapping("/send")
    public R list(@RequestBody SendMsg params) {
        boolean bool = xMessageServiceSendRecordService.autoServiceSendMessage(params);
        return R.ok().setData(bool);
    }

}