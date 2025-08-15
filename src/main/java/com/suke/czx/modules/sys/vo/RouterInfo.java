package com.suke.czx.modules.sys.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description : 路由信息
 * @Date 12:47
 * @Author yzcheng90@qq.com
 **/
@Data
public class RouterInfo {

    public UserInfoVO userInfo;

    public List<SysMenuNewVO> menus;

}