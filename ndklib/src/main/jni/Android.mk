LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := JNI_ANDROID_TEST
LOCAL_SRC_FILES := com_qm_ndklib_JniTest.cpp
include $(BUILD_SHARED_LIBRARY)