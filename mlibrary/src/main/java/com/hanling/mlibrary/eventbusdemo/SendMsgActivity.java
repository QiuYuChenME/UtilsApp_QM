package com.hanling.mlibrary.eventbusdemo;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hanling.mlibrary.R;
import com.hanling.mlibrary.eventbusdemo.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class SendMsgActivity extends AppCompatActivity {
    private Button btn_sendmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        btn_sendmsg = (Button) findViewById(R.id.btn_sendmsg);
        btn_sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
            }
        });

    }
}
