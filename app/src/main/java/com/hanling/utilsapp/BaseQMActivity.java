package com.hanling.utilsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.umeng.message.PushAgent;

public class BaseQMActivity extends AppCompatActivity {
    private PushAgent PushAgentInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_qm);
        getPushAgentInstance().onAppStart();

    }

    /**
     * 獲取Umeng推送的實例化對象
     * @return
     */
    public PushAgent getPushAgentInstance() {
        if (PushAgentInstance != null) {
            return PushAgentInstance;
        } else {
            PushAgentInstance = PushAgent.getInstance(this);
        }
        return PushAgentInstance;
    }
}
