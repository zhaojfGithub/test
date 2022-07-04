package com.zhao.myapplication;

import android.util.Log;

/**
 * 创建时间： 2022/6/30
 * 编   写：  zjf
 * 页面功能:
 */
public class Singleton {

    private static final Singleton SINGLETON = new Singleton();

    public Singleton(){
    }

    public static Singleton getInstance(){
        return SINGLETON;
    }

    private void mLog(){
        Log.e("Singleton","test");
    }
}
