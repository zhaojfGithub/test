package com.zhao.myapplication;

import org.jetbrains.annotations.NotNull;

/**
 * 创建时间： 2022/6/30
 * 编   写：  zjf
 * 页面功能:
 */
public class Singleton {


    @NotNull
    private static final Singleton INSTANCE;


    private Singleton(){}

    static {
        INSTANCE = new Singleton();
    }
}
