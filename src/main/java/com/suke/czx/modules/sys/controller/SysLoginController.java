package com.suke.czx.modules.sys.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.code.kaptcha.Producer;
import com.suke.czx.authentication.detail.CustomUserDetailsService;
import com.suke.czx.authentication.detail.CustomUserDetailsUser;
import com.suke.czx.authentication.utils.TokenUtils;
import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.event.LoginLogEvent;
import com.suke.czx.common.exception.RRException;
import com.suke.czx.common.utils.*;
import com.suke.czx.modules.param.service.TbParamService;
import com.suke.czx.modules.sys.entity.SysLoginLog;
import com.suke.czx.modules.sys.entity.SysUser;
import com.suke.czx.modules.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private final TbParamService tbParamService;
    private final SysUserService sysUserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenUtils tokenUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    @AuthIgnore
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void hello() {

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

    /**
     * 短信验证码
     */
    @AuthIgnore
    @SneakyThrows
    @GetMapping(value = "/sys/mobile/code")
    public R mobileCode(@RequestParam Map<String, Object> params) {
        final String mobile = MapUtil.getStr(params, "mobile");
        final String time = MapUtil.getStr(params, "time");
        String code = generateCode();
        Object sysSuperCode = tbParamService.getValue("sys_super_code");
        if (sysSuperCode != null) {
            JSONObject entries = JSONUtil.parseObj(sysSuperCode.toString());
            Boolean enable = entries.getBool("enable");
            if (enable) {
                code = entries.getStr("code");
            }
        }
        log.info("手机号验证码:{}", code);
        redisTemplate.opsForValue().set(Constant.MOBILE_CODE_KEY + mobile + time, code, 300, TimeUnit.SECONDS);
        return R.ok().setData(Map.of("msgCode", code));
    }

    /**
     * 邮箱验证码
     */
    @AuthIgnore
    @SneakyThrows
    @GetMapping(value = "/sys/email/code")
    public R emailCode(@RequestParam Map<String, Object> params) {
        final String email = MapUtil.getStr(params, "email");
        final String time = MapUtil.getStr(params, "time");
        String code = generateCode();
        Object sysSuperCode = tbParamService.getValue("sys_super_code");
        if (sysSuperCode != null) {
            JSONObject entries = JSONUtil.parseObj(sysSuperCode.toString());
            Boolean enable = entries.getBool("enable");
            if (enable) {
                code = entries.getStr("code");
            }
        }
        log.info("邮箱验证码:{}", code);
        redisTemplate.opsForValue().set(Constant.MOBILE_CODE_KEY + email + time, code, 300, TimeUnit.SECONDS);
        return R.ok().setData(Map.of("emailCode", code));
    }

    @AuthIgnore
    @SneakyThrows
    @PostMapping(value = "/sys/emailOrMobileLogin/login")
    public R emailOrMobileLogin(@RequestParam Map<String, Object> params) {
        this.checkMobileOrEmailCode(params);
        final String type = MapUtil.getStr(params, "type");
        final String username = MapUtil.getStr(params, "username");
        SysUser sysUser;
        if (StrUtil.equals(type, "mobile")) {
            sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getMobile, username));
        } else {
            sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getEmail, username));
        }
        if (sysUser == null) {
            return R.error("账号不存在");
        }
        String token = this.genToken(sysUser);
        return R.ok().put(Constant.TOKEN, token);
    }


    @AuthIgnore
    @PostMapping(value = "/register")
    public R register(@RequestBody SysUser sysUser) {
        String username = sysUser.getUsername();
        String mobile = sysUser.getMobile();
        String email = sysUser.getEmail();
        if (StrUtil.isEmpty(mobile)) {
            return R.error("手机号为空");
        }
        if (StrUtil.isEmpty(email)) {
            return R.error("邮箱为空");
        }
        long usernameCount = sysUserService.count(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (usernameCount > 0) {
            return R.error("用户名已存在");
        }
        long mobileCount = sysUserService.count(Wrappers.<SysUser>query().lambda().eq(SysUser::getMobile, mobile));
        if (mobileCount > 0) {
            return R.error("手机号已注册");
        }
        long emailCount = sysUserService.count(Wrappers.<SysUser>query().lambda().eq(SysUser::getEmail, email));
        if (emailCount > 0) {
            return R.error("邮箱已注册");
        }
        sysUser.setUsername(mobile);
        sysUser.setAuditStatus(1);//审核中
        sysUser.setStatus(0);//禁用
        sysUser.setRoleIdList(List.of(23L)); // 注册的是参赛人员
        sysUser.setCreateTime(new Date());
        sysUserService.saveUserRole(sysUser);
        return R.ok("注册成功，等待审核通过后即可登录");
    }


    /**
     * 生成token
     **/
    public String genToken(SysUser sysUser) {
        if (sysUser.getStatus() == 0) {
            throw new RRException("您的账号已被禁用，请联系管理员");
        }
        if (sysUser.getAuditStatus() != 2) {
            throw new RRException("您的账号未审核通过，请联系管理员");
        }

        // 获取用户
        UserDetails userDetails = customUserDetailsService.loadUserByUserId(sysUser.getUserId());
        if (!userDetails.isEnabled()) {
            throw new RRException("您的账户已被禁用，请联系管理员");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        final HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetailsUser userDetailsUser = (CustomUserDetailsUser) authentication.getPrincipal();
        String userId = userDetailsUser.getUserId();
        String userName = userDetailsUser.getUsername();
        // 保存token
        String token = tokenUtils.generate(userDetailsUser);
        log.info("用户ID:{},用户名:{},登录成功！  token:{}", userId, userName, token);

        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setOptionIp(IPUtils.getIpAddr(request));
        loginLog.setOptionName("用户登录成功");
        loginLog.setOptionTerminal(request.getHeader("User-Agent"));
        loginLog.setUsername(userName);
        loginLog.setOptionTime(new Date());
        SpringContextUtils.publishEvent(new LoginLogEvent(loginLog));
        return token;
    }

    /**
     * 验证短信验证码是否正确
     */
    public void checkMobileOrEmailCode(Map<String, Object> params) {
        if (params == null) {
            throw new RRException("参数错误");
        }
        final String username = MapUtil.getStr(params, "username");
        final String time = MapUtil.getStr(params, "time");
        final String code = MapUtil.getStr(params, "code");
        if (StrUtil.isEmpty(code)) {
            throw new RRException("验证码为空");
        }
        if (StrUtil.isEmpty(time)) {
            throw new RRException("时间戳为空");
        }
        // 验证码KEY
        String key = Constant.MOBILE_CODE_KEY + username + time;
        final Object codeObj = redisTemplate.opsForValue().get(key);
        if (codeObj == null) {
            throw new RRException("验证码过期");
        }
        if (!codeObj.toString().equals(code)) {
            throw new RRException("验证码错误");
        }
        // 删除验证码
        redisTemplate.delete(key);
    }

    private String generateCode() {
        long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        return codeStr.substring(codeStr.length() - 6);
    }
}