package com.qm.customview_qmlibrary;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import static android.R.attr.width;

public class CustomviewActivity extends AppCompatActivity {
    private AutoCompleteTextView autotv_customlib;
    private ArrayAdapter<String> arrayAdapter;
    private FrameLayout rootView;
    private DoubleWaveView waveView;
    private Button btn_dials, btn_topbar, btn_wave, btn_bob;
    private DoubleWaveView doubleWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (true) {
            setContentView(R.layout.activity_customview);
            initView();
            InitListener();
//            autotv_customlib = (AutoCompleteTextView) findViewById(R.id.autotv_customlib);
//            String[] arr = {"aa", "aab", "aac"};
//            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
//            autotv_customlib.setAdapter(arrayAdapter);
//            autotv_customlib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    /** 点击时，弹出下拉框*/
//                    autotv_customlib.showDropDown();
//                }
//            });
//            initWaveView();

        } else {
            /**
             * 测试刻度盘layout
             */
            setContentView(R.layout.activity_flyco);

            /**
             * 上升气泡layout
             */
//            setContentView(R.layout.activity_bob);
        }

    }

    private void InitListener() {
        btn_dials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomviewActivity.this, DialsActivity.class));
            }
        });
        btn_topbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(CustomviewActivity.this, DialsActivity.class));
                Toast.makeText(CustomviewActivity.this, "暂未实现", Toast.LENGTH_SHORT).show();
            }
        });
        btn_wave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomviewActivity.this, WaveActivity.class));
            }
        });
        btn_bob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomviewActivity.this, BobActivity.class));
            }
        });
    }

    private void initView() {
        btn_bob = (Button) findViewById(R.id.btn_bob);
        btn_dials = (Button) findViewById(R.id.btn_dials);
        btn_wave = (Button) findViewById(R.id.btn_wave);
        btn_topbar = (Button) findViewById(R.id.btn_topbar);
        doubleWaveView = (DoubleWaveView) findViewById(R.id.doublewave);
        doubleWaveView.startAnimation();
    }

}
