package com.qm.customview_qmlibrary.gitwave;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gelitenight.waveview.library.*;
import com.gelitenight.waveview.library.WaveView;
import com.qm.customview_qmlibrary.*;
import com.qm.customview_qmlibrary.R;

public class WaveLibActivity extends AppCompatActivity {
    private com.gelitenight.waveview.library.WaveView waveView;

    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.qm.customview_qmlibrary.R.layout.activity_wavelib);
        waveView = (com.gelitenight.waveview.library.WaveView) findViewById(R.id.wv_lib);
        waveView.setBorder(mBorderWidth, mBorderColor);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        WaveHelper waveHelper = new WaveHelper(waveView);
        waveHelper.start();
    }
}
