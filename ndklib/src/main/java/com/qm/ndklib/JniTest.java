package com.qm.ndklib;

/**
 * Created by XueYang on 2017/6/26.
 */

public class JniTest {

    public static native String getPackname(Object o);


    static {
        System.loadLibrary("JNI_ANDROID_TEST");
    }
}