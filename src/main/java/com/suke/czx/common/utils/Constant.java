package com.suke.czx.common.utils;

import lombok.Getter;

/**
 * 常量
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2016年11月15日 下午1:23:52
 */
public class Constant {

    public static final String SYSTEM_NAME = "x-springboot:";
    public static final String SYSTEM_CACHE = "x-springboot:default-cache:";
    public static final String NUMBER_CODE_KEY = SYSTEM_NAME + "number:code:";
    public static final String MOBILE_CODE_KEY = SYSTEM_NAME + "mobile:code:";
    public static final String AUTHENTICATION_TOKEN = SYSTEM_NAME + "token:";

    public static final String TOKEN = "token";
    public static final String TOKEN_ENTRY_POINT_URL = "/token/login";
    public static final String TOKEN_LOGOUT_URL = "/token/logout";
    public static final int TOKEN_EXPIRE = 60 * 60 * 24 * 7;

    /**
     * 超级管理员ID
     */
    public static final String SUPER_ADMIN = "0";
    public static final String SUPER_MINI_ADMIN = "1";

    public static final int CODE_SIZE = 4;

    /**
     * 菜单类型
     *
     * @author czx
     * @email object_czx@163.com
     * @date 2016年11月15日 下午1:24:29
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    @Getter
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU("qiniu"),
        /**
         * 阿里云
         */
        ALIYUN("aliyun"),
        /**
         * 腾讯云
         */
        QCLOUD("qcloud"),
        /**
         * minio
         */
        MINIO("minio");

        private final String value;

        CloudService(String value) {
            this.value = value;
        }
    }

}
