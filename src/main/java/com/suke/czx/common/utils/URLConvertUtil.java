package com.suke.czx.common.utils;

/**
 * @author czx
 * @title: URLConvertUtil
 * @projectName zhjg
 * @description: TODO rest url 转驼峰
 * @date 2020/5/18 11:46
 */
public class URLConvertUtil {

    private static final char UNDERLINE = '/';
    private static final String INDEX = "/{";

    public static String convert(String url) {
        if(url.contains(INDEX)){
            url = url.substring(0,url.indexOf(INDEX));
        }
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<url.length();i++){
            char c = url.charAt(i);
            if(i == 0){
                if(c == UNDERLINE){
                    sb.append(Character.toUpperCase(url.charAt(i+1)));
                    i++;
                }else {
                    sb.append(Character.toUpperCase(url.charAt(i)));
                }
            }else {
                if(c == UNDERLINE){
                    sb.append(Character.toUpperCase(url.charAt(i+1)));
                    i++;
                }else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String capture(String url) {
        if(url.contains(INDEX)){
            url = url.substring(0,url.indexOf(INDEX)) + "/**";
        }
        return url;
    }

}
