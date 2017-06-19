package com.hanling.mlibrary;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hanling.mlibrary.eventbusdemo.EventBusActivity;
import com.hanling.mlibrary.greendaodemo.GreenDaoActivity;
import com.hanling.mlibrary.retrofitdemo.RetrofitActivity;

public class LibMainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private ActionBar actionBar;
    private Button btn_one_mlib, btn_greendao;
    private Button btn_three_mlib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_lib);
//        actionBar = getActionBar();
//        actionBar.hide(); //会报错
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs

        /**
         * 调用相机  （不需要权限）
         */
//        Intent intent = new Intent(); //调用照相机
//        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
//        startActivity(intent);
        /**
         * 开启蓝牙  （需要权限） android.permission.BLUETOOTH_ADMIN
         */
        // bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // bluetoothAdapter.enable();
        initView();
    }

    private void initView() {
        btn_one_mlib = (Button) findViewById(R.id.btn_one_mlib);
        btn_greendao = (Button) findViewById(R.id.btn_two_mlib);
        btn_three_mlib = (Button) findViewById(R.id.btn_three_mlib);
        btn_one_mlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LibMainActivity.this, EventBusActivity.class);
                startActivity(i);
            }
        });
        btn_greendao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LibMainActivity.this, GreenDaoActivity.class);
                startActivity(i);
            }
        });
        btn_three_mlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LibMainActivity.this, RetrofitActivity.class);
                startActivity(i);
            }
        });
    }
}
