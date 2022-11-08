package com.cstore.nativelib;

public class NativeLib {

    // Used to load the 'nativelib' library on application startup.

    public static String getJniString(){
        return stringFromJNI();
    }

    //jni
    static {
        System.loadLibrary("nativelib");
    }



    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    //jni
    public static native String stringFromJNI();
}