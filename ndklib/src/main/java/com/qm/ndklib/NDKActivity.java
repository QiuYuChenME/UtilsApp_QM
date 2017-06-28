package com.qm.ndklib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NDKActivity extends AppCompatActivity {
    private TextView tv_ndk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk);
        tv_ndk = (TextView) findViewById(R.id.tv_ndk);
        tv_ndk.setText(JniTest.getPackname(new Object()));


    }
}
