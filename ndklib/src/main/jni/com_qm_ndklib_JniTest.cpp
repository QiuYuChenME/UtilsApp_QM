//
// Created by XueYang on 2017/6/26.
//
#include <stdio.h>
#include <jni.h>
#include <stdlib.h>
#include "com_qm_ndklib_JniTest.h"
JNIEXPORT jstring JNICALL Java_com_qm_ndklib_JniTest_getPackname
  (JNIEnv *env, jclass clazz, jobject){

      return env->NewStringUTF("I am from C String!");
  }