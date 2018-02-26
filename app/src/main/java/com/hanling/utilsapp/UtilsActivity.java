package com.hanling.utilsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import com.google.gson.Gson;
import com.hanling.mlibrary.LibMainActivity;
import com.hanling.utilsapp.MVPdemo.view.MVPLoginTestActivity;

import com.hanling.utilsapp.bean.ResultBean;
import com.hanling.xfvoicelibrary.XFLibVoiceActivity;
import com.orhanobut.logger.Logger;
import com.qm.customview_qmlibrary.CustomviewActivity;
import com.qm.maplib.MapActivity;
import com.qm.ndklib.NDKActivity;
import com.qm.sanforvpnconnlib.SanforConnActivity;
import com.qyc.bluetoothlibrary.BlueToothActivity;
import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SangforAuth;
import com.umeng.message.PushAgent;


import java.io.IOException;
import java.util.Random;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


public class UtilsActivity extends BaseQMActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private PushAgent pushAgentInstance;
    private Context context;
    private static final String TAG = "UtilsActivity";
    @BindView(R.id.btn_utils)
    Button btn_utils;
    @BindView(R.id.btn_bluetoothlib)
    Button btn_bluetoothlib;
    @BindView((R.id.btn_network))
    Button btn_network;
    private String[] permisson_group = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};
    private int permission_flg = 1;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils);
        ButterKnife.bind(this);
        context = this;

        //初始化UI控件
        initView();
        //线程中ping 网址 测试网络是否正常
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean b = pingIpAddress("10.14.10.24");
                Log.e(TAG, "run: " + b);
            }
        }).start();

        // 手动获取device token
        pushAgentInstance = getPushAgentInstance();
        String registrationId = pushAgentInstance.getRegistrationId();
        Logger.d(TAG + "device_token:" + registrationId);
//       基础弹窗
//       showMyDialog()
//       动态申请权限
//       getPermission()
    }

    /**
     * 弹框
     */
    private void showMyDialog() {
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(UtilsActivity.this);
        //    设置Title的图标

        //    设置Title的内容
        builder.setTitle("弹出警告框");
        //    设置Content来显示一个信息
        builder.setMessage("确定删除吗？");
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UtilsActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UtilsActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //    设置一个NeutralButton
        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UtilsActivity.this, "neutral: " + which, Toast.LENGTH_SHORT).show();
            }
        });
        //    显示出该对话框
        builder.show();
    }

    /**
     * 动态监测权限并申请 （略：申请回调）
     */
    private void getPermission() {
        //请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UtilsActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);
                Logger.d("动态请求READ_CONTACTS权限");
            }

        }
    }

    private void initView() {
        btn_utils.setText(R.string.btnutils);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utils, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, LibMainActivity.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, XFLibVoiceActivity.class));
            finish();
        } else if (id == R.id.nav_slideshow) {

            startActivity(new Intent(this, MVPLoginTestActivity.class));
        } else if (id == R.id.nav_manage) {
            if (SangforAuth.getInstance().vpnQueryStatus() != IVpnDelegate.VPN_STATUS_AUTH_OK) {
                Intent i = new Intent(this, SanforConnActivity.class);
                startActivity(i);
            }

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this, NDKActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_send) {
            Intent i = new Intent(this, CustomviewActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_map) {
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 黄油刀点击事件
     *
     * @param button
     */
    @OnClick({R.id.btn_utils, R.id.btn_bluetoothlib, R.id.btn_network})
    public void A(Button button) {

        switch (button.getId()) {
            case R.id.btn_utils:
                Toast.makeText(this, "被点击了" + button.getText(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bluetoothlib:
                Intent intent = new Intent(UtilsActivity.this, BlueToothActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_network:
                Toast.makeText(context, getNetStatus() + "", Toast.LENGTH_SHORT).show();
                break;
        }


    }


    /**
     * ping 网络命令
     */
    private boolean pingIpAddress(String ipAddress) {
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 5 " + ipAddress);
            int status = process.waitFor();
            if (status == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 弹出notifycation
     */
    public void showNotifycation() {

        int id = new Random(System.nanoTime()).nextInt();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle("111111111")
                .setContentText("111111111")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_fsas)
                .setAutoCancel(true);
        Notification notification = mBuilder.getNotification();
        manager.notify(id, notification);
    }

    /**
     *
     */
    public void showGson() {
        Gson gson = new Gson();
        ResultBean resultBean = gson.fromJson("{\n" +
                "    \"msg\": {\n" +
                "        \"username\": [\n" +
                "            \"该字段是必填项。\"\n" +
                "        ],\n" +
                "        \"password\": [\n" +
                "            \"该字段是必填项。\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"code\": 4,\n" +
                "    \"resultObj\": []\n" +
                "}", ResultBean.class);
        Log.e(TAG, "onCreate: " + resultBean.getMsg());
        int i = (0x2) >> 1;
        Log.e(TAG, "useAppContext: " + i);
    }

    /**
     * 判读当前网络状态
     */
    public boolean getNetStatus() {
        boolean flag;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        flag = activeNetworkInfo != null && activeNetworkInfo.isAvailable();
        return flag;
    }
}
