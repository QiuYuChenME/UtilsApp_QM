package com.qm.customview_qmlibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WaveView extends View {
    private String TAG = this.getClass().getSimpleName();
    public static float HIGH_LEVEL_LINE_PERCENT = 30f;
    public static float LOW_LEVEL_LINE_PERCENT = 80f;

    private int mViewWidth;
    private int mViewHeight;

    private float mLevelLinePercent = HIGH_LEVEL_LINE_PERCENT;

    /**
     * 水位线
     */
    private float mLevelLine;

    /**
     * 波浪起伏幅度
     */
    private float mWaveHeight = 80;
    /**
     * 波长
     */
    private float mWaveWidth = 200;
    /**
     * 被隐藏的最左边的波形
     */
    private float mLeftSide;

    private float mMoveLen;
    /**
     * 水波平移速度
     */
    public static final float SPEED = 10f;
    private String wave2TopColor = "#89A0CDEA";
    private String wave2BottomColor = "#89A0CDEA";
    private String wave1TopColor = "#8979acfe";
    private String wave1BottomColor = "#8910429e";
//    private String wave2TopColor = "#c97ed0fe";
//    private String wave2BottomColor = "#c9579cf6";

    private List<Point> mPointsList;
    private List<Point> mPointsList2;
    private Paint mPaint;// line1
    private Paint mPaint2;// line2
    private Paint mTextPaint;
    private Path mWavePath;
    private Path mWavePath2;
    private boolean isMeasured = false;

    private Timer timer;


    private boolean isShowBubbles = false;
    private Paint paint;
    private Random rand = new Random();
    private int height, width;
    private boolean isInitBubble = false;
    private static int count = 20;
    private ArrayList<Circle> bubbles = new ArrayList<Circle>();
    private int dx;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        width = w_screen;
        height = h_screen;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
        // mPaint.setColor(Color.parseColor("#ef3f639e"));

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Style.FILL);
        // mPaint2.setColor(Color.parseColor("#9f63A9E4"));

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.setTextSize(30);

        mWavePath = new Path();
        mWavePath2 = new Path();

        paint = new Paint();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rectaround = new Rect(0, 0, getWidth(), getHeight());
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        canvas.drawRect(rectaround, p);
        resetPaint();

        Rect rect = new Rect(0, 0, mViewWidth, getHeight());
        Paint paint_around = new Paint();
        paint_around.setStyle(Style.STROKE);
        paint_around.setColor(Color.RED);
        paint_around.setStrokeWidth(10);
        canvas.drawRect(rect, paint_around);
        mWavePath.reset();
        int m = 0;
        mWavePath.moveTo(-width + dx, height / 5 * 1);
        for (int i = 0; i < 3; i++) {
            mWavePath.rQuadTo(width / 4, -dip2px(getContext(), 15), width / 2, 0);
            mWavePath.rQuadTo(width / 4, dip2px(getContext(), 15), width / 2, 0);
        }
        mWavePath.lineTo(width, height);
        mWavePath.lineTo(0, height);
        mWavePath.close();

        mWavePath2.reset();
        int j = 0;
        mWavePath2.moveTo(-width + dx, height / 5 * 1);
        for (int i = 0; i < 3; i++) {
            mWavePath2.rQuadTo(width / 4, dip2px(getContext(), 15), width / 2, 0);
            mWavePath2.rQuadTo(width / 4, -dip2px(getContext(), 15), width / 2, 0);
        }
        mWavePath2.lineTo(width, height);
        mWavePath2.lineTo(0, height);
        mWavePath2.close();

        // mPaint的Style是FILL，会填充整个Path区域
        canvas.drawPath(mWavePath, mPaint);
        canvas.drawPath(mWavePath2, mPaint2);

        if (isShowBubbles) {
            if (!isInitBubble) {
                height = this.getHeight();
                width = this.getWidth();
                isInitBubble = true;
                for (int i = 0; i < count; i++) {
                    bubbles.add(new Circle(rand.nextInt(width), rand.nextInt(height / 2) + height / 2, rand.nextInt(dip2px(getContext(), 13)) + dip2px(getContext(), 3),
                            rand.nextInt(60) + 40));
                }
            }

            paint.reset();
            for (Circle c : bubbles) {
                c.increase();
            }

            for (Circle c : bubbles) {
                c.draw(canvas, paint);
            }
        } else {
            isInitBubble = false;
            bubbles.clear();
        }

    }

    private void resetPaint() {
        // 设置深蓝色的渐变色
        LinearGradient lg = new LinearGradient(0, mLevelLine - mWaveHeight, 0, getHeight(),
                Color.parseColor(wave1TopColor), Color.parseColor(wave1BottomColor), Shader.TileMode.REPEAT);
        mPaint.setShader(lg);
        // 设置浅蓝色的渐变色
        LinearGradient lg2 = new LinearGradient(0, mLevelLine - mWaveHeight, 0, getHeight(),
                Color.parseColor(wave2TopColor), Color.parseColor(wave2BottomColor), Shader.TileMode.REPEAT);
        mPaint2.setShader(lg2);
    }


    class Point {
        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }

    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, width);
        valueAnimator.setDuration(4000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private class CircleCenterPoint {
        private float x;
        private float y;

        public CircleCenterPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public boolean isOutOfView(float r) {
            boolean isOutView = false;
            if (x > width - r || x < r || y > height || y <= height / 2) {
                isOutView = true;
            }

            return isOutView;
        }

        public boolean isInCenterView(float r, float centerViewR) {
            boolean isOutView = false;
            if (x > width / 2 - centerViewR && x < width / 2 + centerViewR && y > height / 2 - centerViewR
                    && y <= height / 2 + centerViewR) {
                isOutView = true;
            }
            return isOutView;
        }
    }

    private class Circle {
        private CircleCenterPoint center;
        private float r;
        private int a;
        private int aMax;
        private double distance;
        private double minDistance;
        private int type = rand.nextInt(100);
        private int count = 0;

        private double delta = 0.1f;
        private double theta = 0;

        private boolean isInitDistance = false;
        boolean isShouldDisapear = false;

        public Circle(float x, float y, float r, int a) {
            center = new CircleCenterPoint(x, y);
            this.r = r;
            this.a = a;
            minDistance = rand.nextInt(100);
        }


        public void increase() {
            if (isInitDistance) {
                delta = 0.1f;
                isInitDistance = false;
            }
            changeCenterPoint(center, type);

            if (center.isOutOfView(r) || center.isInCenterView(r, 100f)) {
                isShouldDisapear = true;

            }

        }

        public void draw(Canvas canvas, Paint paint) {
            count++;
            if (count % ((type + 8) * 10) == 0) {
                type = rand.nextInt(100);
            }
            if (isShouldDisapear) {
                a--;
                if (a <= 0) {
                    isShouldDisapear = false;
                    center.setX(rand.nextInt(width));
                    center.setY(rand.nextInt(height / 2) + height / 2);
                    r = rand.nextInt(dip2px(getContext(), 13)) + dip2px(getContext(), 3);
                    aMax = rand.nextInt(60) + 40;
                }

            } else {

                if (a <= aMax) {
                    a++;
                } else {

                    aMax = 0;
                }
            }
            paint.reset();
            paint.setColor(Color.WHITE);

            //TODO 部分透明度a为负数会导致闪白，暂无更好解决办法，只能if过滤
            if (a < 0) {
                paint.setAlpha(0);
            } else {
                paint.setAlpha(a);
            }

            canvas.drawCircle(center.getX(), center.getY(), r, paint);
        }

        private double getDistance(double distance) {

            double result = distance;
            delta = delta + 0.1;

            result = result - delta * delta;
            if (result < minDistance) {
                result = minDistance;
            }
            return result;
        }
    }

    private void changeCenterPoint(CircleCenterPoint center, int type) {
        center.setY(center.getY() - 8);
        center.setX(center.getX() + (float) (width / 200 * Math.sin(center.getY() / 100)));
    }

    public void showBubbles() {
        isShowBubbles = true;
    }

    public void dismissBubbles() {
        isShowBubbles = false;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
