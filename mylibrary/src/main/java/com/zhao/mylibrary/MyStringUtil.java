package com.zhao.mylibrary;

/**
 * 创建时间： 2022/6/24
 * 编   写：  zjf
 * 页面功能:
 */
public class MyStringUtil {
    public static String ndkString(){
        return getStringNdk();
    }

    private static native String getStringNdk();

    static {
        System.loadLibrary("mylibrary");
    }
}
