package com.hanling.utilsapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

import java.util.Random;

/**
 * Developer defined push intent service.
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 *
 * @author lucas
 */
//完全自定义处理类
public class MyPushIntentService extends UmengMessageService {
    private static final String TAG = MyPushIntentService.class.getName();

    @Override
    public void onMessage(Context context, Intent intent) {
        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            UmLog.d(TAG, "message=" + message);    //消息体
            UmLog.d(TAG, "custom=" + msg.custom);    //自定义消息的内容
            UmLog.d(TAG, "title=" + msg.title);    //通知标题
            UmLog.d(TAG, "text=" + msg.text);    //通知内容
            // code  to handle message here
            // ...
            if (msg.display_type.equals("notification")) {

                //notifycation
                int id = new Random(System.nanoTime()).nextInt();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();
                Notification.Builder mBuilder = new Notification.Builder(this);
                JSONObject jsonObject = new JSONObject(msg.custom);
                String content = (String) jsonObject.get("content");
                mBuilder.setContentTitle(msg.title)
                        .setContentText(msg.text)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.icon_fsas)
                        .setAutoCancel(true);
                Notification notification = mBuilder.getNotification();
//                PendingIntent clickPendingIntent = getClickPendingIntent(this, msg);
//                PendingIntent dismissPendingIntent = getDismissPendingIntent(this, msg);
//                notification.deleteIntent = dismissPendingIntent;
//                notification.contentIntent = clickPendingIntent;
                manager.notify(id, notification);
            } else if (msg.display_type.equals("custom")) {

                //notifycation
                int id = new Random(System.nanoTime()).nextInt();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();
                Notification.Builder mBuilder = new Notification.Builder(this);
                mBuilder.setContentTitle("提示")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.icon_fsas)
                        .setAutoCancel(true);
                try {
                    JSONObject jsonObject = new JSONObject(msg.custom);
                    String content = (String) jsonObject.get("content");
                    mBuilder.setContentText(content);
                } catch (Exception e) {
                    mBuilder.setContentText(msg.custom);
                }


                Notification notification = mBuilder.getNotification();
//                PendingIntent clickPendingIntent = getClickPendingIntent(this, msg);
//                PendingIntent dismissPendingIntent = getDismissPendingIntent(this, msg);
//                notification.deleteIntent = dismissPendingIntent;
//                notification.contentIntent = clickPendingIntent;
                manager.notify(id, notification);
                //发送广播
                Intent tipIntent = new Intent();
                tipIntent.setAction("tip");
                tipIntent.putExtra("tip", msg.custom);
                context.sendBroadcast(tipIntent);

            }
            // 对完全自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if (isClickOrDismissed) {
                //完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                //完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }
        } catch (Exception e) {
            UmLog.e(TAG, e.getMessage());
        }
    }
//
//    public PendingIntent getClickPendingIntent(Context context, UMessage msg) {
//        Intent clickIntent = new Intent();
//        clickIntent.setClass(context, NotificationBroadcast.class);
//        clickIntent.putExtra(NotificationBroadcast.EXTRA_KEY_MSG,
//                msg.getRaw().toString());
//        clickIntent.putExtra(NotificationBroadcast.EXTRA_KEY_ACTION,
//                NotificationBroadcast.ACTION_CLICK);
//        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context,
//                (int) (System.currentTimeMillis()),
//                clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        return clickPendingIntent;
//    }
//
//    public PendingIntent getDismissPendingIntent(Context context, UMessage msg) {
//        Intent deleteIntent = new Intent();
//        deleteIntent.setClass(context, NotificationBroadcast.class);
//        deleteIntent.putExtra(NotificationBroadcast.EXTRA_KEY_MSG,
//                msg.getRaw().toString());
//        deleteIntent.putExtra(
//                NotificationBroadcast.EXTRA_KEY_ACTION,
//                NotificationBroadcast.ACTION_DISMISS);
//        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context,
//                (int) (System.currentTimeMillis() + 1),
//                deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        return deletePendingIntent;
//    }

}
