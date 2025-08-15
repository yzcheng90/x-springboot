package com.suke.czx.modules.sys.controller;

import cn.hutool.core.io.IoUtil;
import com.google.code.kaptcha.Producer;
import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关
 *
 * @author czx
 * @email object_czx@163.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "SysLoginController", description = "登录相关")
public class SysLoginController extends AbstractController {

    private final Producer producer;
    private final RedisTemplate<Object,Object> redisTemplate;

    @AuthIgnore
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public R hello() {
        return R.ok("hello welcome to use x-springboot");
    }

    /**
     * 验证码
     */
    @AuthIgnore
    @SneakyThrows
    @RequestMapping(value = "/sys/code/{time}", method = RequestMethod.GET)
    public void captcha(@PathVariable("time") String time, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        log.info("验证码:{}", text);
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //redis 60秒
        redisTemplate.opsForValue().set(Constant.NUMBER_CODE_KEY + time, text, 60, TimeUnit.SECONDS);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IoUtil.close(out);
    }
}