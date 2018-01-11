package com.qm.customview_qmlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        WaveView wave = (WaveView) findViewById(R.id.wave);
        wave.showBubbles();
        wave.startAnimation();
    }
}
