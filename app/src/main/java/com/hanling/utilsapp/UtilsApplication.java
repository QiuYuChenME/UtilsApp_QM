
package com.hanling.utilsapp;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

public class UtilsApplication extends Application {
    private String TAG = getClass().getName();

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {

                Logger.d(s);
            }

            @Override
            public void onFailure(String s, String s1) {
                Logger.d(s);
                Logger.d(s1);
            }
        });
        /**
         * 設置完全自定義消息后，需要使用此方法讓自定義接收失效  。。。
         * 不想使用完全自定義消息 需要在前面設置此條
         */
        mPushAgent.setPushIntentServiceClass(null);
        /**
         * Umeng 發送通知 ，後續動作自定義字符串可以通過該方法取到
         */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            /**
             * 此方法通知點擊時回調
             * @param context
             * @param msg
             */
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }


        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        /**
         * 設置通知欄 推送的條數
         */
        mPushAgent.setDisplayNotificationNumber(2);
        /**
         * 使用Umeng自定义消息以及 通知欄回調
         */
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();

                    }
                });
            }

            /**
             * 此方法会在通知展示到通知栏时回调
             *
             * @param context
             * @param uMessage
             * @return
             */
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                Log.e(TAG, "getNotification: " + uMessage.toString());
                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
        /**
         * 設置完全自定義消息
         */
//        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);

    }

}

                                