package com.qm.customview_qmlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by QYC on 2017/12/26.
 */

public class ViewW extends View {
    public ViewW(Context context) {
        super(context);
    }

    public ViewW(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewW(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p_wave = new Paint();
        p_wave.setColor(Color.parseColor("#8979acfe"));
        p_wave.setStyle(Paint.Style.STROKE);

        Path path = new Path();
//        path.moveTo(-w);
    }
}
