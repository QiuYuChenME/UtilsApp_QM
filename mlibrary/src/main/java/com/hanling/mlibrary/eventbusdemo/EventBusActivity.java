package com.hanling.mlibrary.eventbusdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hanling.mlibrary.R;
import com.hanling.mlibrary.eventbusdemo.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.logging.Logger;

public class EventBusActivity extends AppCompatActivity {
    private Button btn_eventbus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        btn_eventbus = (Button) findViewById(R.id.btn_eventbus);
        btn_eventbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventBusActivity.this,SendMsgActivity.class);
                startActivity(i);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(MessageEvent event) {
        com.orhanobut.logger.Logger.d(event.message);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
