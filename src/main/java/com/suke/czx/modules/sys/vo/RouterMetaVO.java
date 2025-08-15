package com.suke.czx.modules.sys.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 路由元信息
 * @Date 12:26
 * @Author yzcheng90@qq.com
 **/
@Data
public class RouterMetaVO implements Serializable {

    public String title;
    public String isLink;
    public boolean isHide;
    public boolean isKeepAlive;
    public boolean isAffix;
    public boolean isIframe;
    public String icon;
    public String roles;

}