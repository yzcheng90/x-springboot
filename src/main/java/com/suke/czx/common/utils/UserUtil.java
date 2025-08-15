package com.suke.czx.common.utils;

import cn.hutool.core.util.StrUtil;
import com.suke.czx.authentication.detail.CustomUserDetailsUser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Description
 * @Date 10:20
 * @Author yzcheng90@qq.com
 **/
@UtilityClass
public class UserUtil {

    public CustomUserDetailsUser getUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object != null) {
            return (CustomUserDetailsUser) object;
        }
        return null;
    }

    @SneakyThrows
    public String getUserId() {
        return getUser() == null ? null : getUser().getUserId();
    }

    @SneakyThrows
    public Integer getUserTenancyId() {
        return getUser() == null ? null : getUser().getTenancyId();
    }

    public boolean isAdmin(String userId) {
        return StrUtil.isNotEmpty(userId) && userId.equals(Constant.SUPER_ADMIN);
    }

    public boolean isAdmin() {
        return getUser() != null && getUser().getUserId().equals(Constant.SUPER_ADMIN);
    }

    public boolean isMiniAdmin() {
        return getUser() != null && getUser().getUserId().equals(Constant.SUPER_MINI_ADMIN);
    }

    public boolean isMiniAdmin(String userId) {
        return StrUtil.isNotEmpty(userId) && userId.equals(Constant.SUPER_MINI_ADMIN);
    }


}
