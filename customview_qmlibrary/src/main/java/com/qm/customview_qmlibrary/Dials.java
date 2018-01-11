package com.qm.customview_qmlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 圆形刻度盘   适用范围：flyco 加湿器 目标湿度
 * <p>
 * 注滑动以10dp做单位
 * Created by QYC on 2017/12/19.
 */

public class Dials extends View {
    private static final String TAG = "ViewT";
    float endX = 0;
    float startX = 0;
    private int Staticpercent = 0;
    private Paint testPaint;

    //x轴滑动差值
    private int d_width;
    private int progress = -30;
    private int text_num = 30;
    private boolean isDown;
    private boolean isMove;

    public Dials(Context context) {
        super(context);
        initTestPaint();
    }


    public Dials(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTestPaint();
    }

    public Dials(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTestPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint forePaint = new Paint();
        int mWidth = getWidth();
        int mHeight = getHeight();
        int radius = dip2px(getContext(), 230) / 2;

        forePaint.setStyle(Paint.Style.STROKE);
        forePaint.setAntiAlias(true);
        forePaint.setStrokeWidth(dip2px(getContext(), 15));
        forePaint.setColor(0xfff0b921);
        // 测试圆心
//        canvas.drawCircle(mWidth / 2, mWidth / 2, dip2px(getContext(), 230), forePaint);

        //线末端的样式
//        forePaint.setStrokeCap(Paint.Cap.ROUND);

        //绘制区域大小
        RectF mPaintRectF = new RectF(mWidth / 2 - radius, mHeight / 2 - radius, mWidth / 2 + radius, mHeight / 2 + radius);
        //显示绘制区的边界
        canvas.drawRect(mPaintRectF, testPaint);

        //绘制刻度盘
        canvas.save();
        forePaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.rotate(150, mWidth / 2, mHeight / 2);
        canvas.drawArc(mPaintRectF, 0, 60, false, forePaint);

        forePaint.setColor(0xff3acaff);
        forePaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(mPaintRectF, 140, 100, false, forePaint);
        forePaint.setColor(0xff07a84b);
        forePaint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawArc(mPaintRectF, 60, 80, false, forePaint);

        canvas.restore();


        //刻度
        setLine(canvas);
        //中部文字显示
        setCenterText(canvas);
        //控制小球滚动
        setMove(canvas);
    }

    /**
     * 画刻度
     *
     * @param canvas
     */
    private void setLine(Canvas canvas) {

        int radius = dip2px(getContext(), 230) / 2;
        Paint paint_line = new Paint();
        paint_line.setStrokeWidth(dip2px(getContext(), 1));
        paint_line.setColor(Color.WHITE);
        paint_line.setStyle(Paint.Style.STROKE);
        Paint paint_word = new Paint();
        paint_word.setLinearText(true);
        paint_word.setTextSize(dip2px(getContext(), 11));
        paint_word.setColor(Color.WHITE);

        //设置Path
        Path path = new Path();
        //5代表刻度与刻度盘圆环的间距，8为刻度盘圆环宽度的1/2
        path.moveTo(getWidth() / 2 - radius + dip2px(getContext(), 4 + 8), getHeight() / 2);
        path.lineTo(getWidth() / 2 - radius + dip2px(getContext(), 4 + 8 + 4), getHeight() / 2);
        //刻度绘制
        for (int i = 0; i < 7; i++) {
            canvas.save();
            int angle = -30;
            canvas.rotate(angle + i * 40, getWidth() / 2, getHeight() / 2);
            canvas.drawPath(path, paint_line);
            canvas.restore();
        }
//        Rect mBound = new Rect();
//        paint_word.getTextBounds(String.valueOf(30), 0, String.valueOf(30).length(), mBound);
//        canvas.drawText(String.valueOf(90), 0, String.valueOf(90).length(), getWidth() / 2 - radius + dip2px(getContext(), 4 + 8 + 4 + 4), getHeight() / 2 + mBound.height() / 2, paint_word);
        //刻度文字绘制
        for (int i = -3; i < 4; i++) {

            canvas.save();
            canvas.rotate(i * 40, getWidth() / 2, getHeight() / 2);
            Rect mBound = new Rect();
            paint_word.getTextBounds(String.valueOf(30), 0, String.valueOf(30).length(), mBound);
            canvas.drawText(String.valueOf(60 + i * 10), 0, String.valueOf(90).length(), getWidth() / 2 - mBound.width() / 2, getHeight() / 2 - radius + dip2px(getContext(), 4 + 8 + 4 + 4) + mBound.height(), paint_word);
            canvas.restore();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //小球滚动
    public void setMove(Canvas canvas) {
        int radius = dip2px(getContext(), 230) / 2;
        canvas.save();

        if (d_width / (dip2px(getContext(), 10)) * 4 + progress < -30) {
            canvas.rotate(-30, getWidth() / 2, getHeight() / 2);
        } else if (d_width / (dip2px(getContext(), 10)) * 4 + progress > 210) {
            canvas.rotate(210, getWidth() / 2, getHeight() / 2);
        } else {
            canvas.rotate(d_width / (dip2px(getContext(), 10)) * 4 + progress, getWidth() / 2, getHeight() / 2);

        }
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth() / 2 - radius, getHeight() / 2, dip2px(getContext(), 15), paint);
        canvas.restore();
    }

    /**
     * 刻度盘中间文字
     *
     * @param canvas
     */
    public void setCenterText(Canvas canvas) {
        Rect mBound = new Rect();
        TextPaint tp = new TextPaint();
        tp.setColor(Color.WHITE);
        tp.setTextSize(dip2px(getContext(), 60));
        Log.e(TAG, "setCenterText: " + text_num + "---" + d_width / (dip2px(getContext(), 10)));

        Log.e(TAG, "setCenterText: " + (text_num + d_width / (dip2px(getContext(), 10))));
        if (text_num + d_width / (dip2px(getContext(), 10)) > 90) {
            tp.getTextBounds(String.valueOf(90), 0, String.valueOf(90).length(), mBound);
            canvas.drawText(String.valueOf(90), getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, tp);
        } else if (text_num + d_width / (dip2px(getContext(), 10)) < 30) {
            tp.getTextBounds(String.valueOf(30), 0, String.valueOf(30).length(), mBound);
            canvas.drawText(String.valueOf(30), getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, tp);

        } else {
            tp.getTextBounds(String.valueOf(text_num + d_width / (dip2px(getContext(), 10))), 0, String.valueOf(text_num + d_width / (dip2px(getContext(), 10))).length(), mBound);
            canvas.drawText(String.valueOf(text_num + d_width / (dip2px(getContext(), 10))), getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, tp);
        }
        Paint paint_percent = new Paint();
        paint_percent.setTextSize(dip2px(getContext(), 15));
        paint_percent.setColor(Color.WHITE);
        canvas.drawText("%", 0, "%".length(), getWidth() / 2 + mBound.width() / 2 + dip2px(getContext(), 7), getHeight() / 2 + mBound.height() / 2, paint_percent);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                isDown = true;
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE://移动

                endX = event.getX();

                //TODO  滑动距离双倍，再次点击时会回弹
                d_width = (int) (endX - startX);
                invalidate();
                isMove = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP://松开

                if (isMove && isDown) {

                    progress = d_width / (dip2px(getContext(), 10)) * 4 + progress;
                    text_num = text_num + d_width / (dip2px(getContext(), 10));
                    d_width = 0;
                    invalidate();
                    if (text_num > 90) {
                        text_num = 90;
                    } else if (text_num < 30) {
                        text_num = 30;
                    }
                    if (progress < -30) {
                        progress = -30;
                    } else if (progress > 210) {
                        progress = 210;
                    }


                    isDown = false;
                    isMove = false;
                }

                break;
        }

        return true;
    }

    /**
     * 测试用绘制范围的画笔
     */
    private void initTestPaint() {
        testPaint = new Paint();
        testPaint.setColor(Color.argb(255, 17, 79, 152));
        testPaint.setStrokeWidth(10);
        testPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }


}
