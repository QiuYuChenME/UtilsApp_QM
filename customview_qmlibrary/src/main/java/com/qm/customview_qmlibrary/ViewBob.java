package com.qm.customview_qmlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by QYC on 2017/12/21.
 */

public class ViewBob extends View {
    private int pro = 0;

    public ViewBob(Context context) {
        super(context);
    }

    public ViewBob(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewBob(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pro++;
            }
        }).start();

        Paint p = new Paint();
        p.setTextSize(100);
        p.setColor(Color.RED);
        canvas.drawText(String.valueOf(pro), getWidth() / 2, getHeight() / 2, p);
        invalidate();
    }
}
