package com.qm.customview_qmlibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by QYC on 2017/12/19.
 */

public class DoubleWaveView extends View {

    private Paint mPaint;
    private Path mPath;
    private int width, height;
    private boolean starting = false;
    private int dx;
    private List<Bubble> bubbles = new ArrayList<Bubble>();
    private Random random = new Random();//生成随机数;

    public DoubleWaveView(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
        init();
    }

    public DoubleWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoubleWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        width = w_screen;
        height = h_screen ;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Bobe(canvas);
        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        canvas.drawRect(rect, p);
        mPath.reset();
        mPaint.setColor(Color.parseColor("#659ccdeb"));
        mPath.moveTo(-width + dx, height / 5 * 3);
        for (int i = 0; i < 3; i++) {
            mPath.rQuadTo(width / 4, -dip2px(getContext(), 15), width / 2, 0);
            mPath.rQuadTo(width / 4, dip2px(getContext(), 15), width / 2, 0);
        }
        //修改 height可以变高
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        mPaint.setColor(Color.parseColor("#653e75ae"));
        mPath.moveTo(-width + dx, height / 5 * 3);
        for (int i = 0; i < 3; i++) {
            mPath.rQuadTo(width / 4, dip2px(getContext(), 15), width / 2, 0);
            mPath.rQuadTo(width / 4, -dip2px(getContext(), 15), width / 2, 0);
        }
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

    }

    private void Bobe(Canvas canvas) {
        if (!starting) {
            starting = true;
            new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(random.nextInt(3) * 300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Bubble bubble = new Bubble();
                        int radius = random.nextInt(30);
                        while (radius == 0) {
                            radius = random.nextInt(30);
                        }
                        float speedY = random.nextFloat() * 5;
                        while (speedY < 1) {
                            speedY = random.nextFloat() * 5;
                        }
                        bubble.setRadius(radius);
                        bubble.setSpeedY(speedY);
                        bubble.setX(width / 2);
                        bubble.setY(height);
                        float speedX = random.nextFloat() - 0.5f;
                        while (speedX == 0) {
                            speedX = random.nextFloat() - 0.5f;
                        }
                        bubble.setSpeedX(speedX * 2);
                        if (bubbles.size() <= 100)
                            bubbles.add(bubble);
                    }
                }
            }.start();
        }
        Paint paint = new Paint();
        // 绘制渐变正方形作为背景
        // 我更倾向于用图片做背景
        /*
        Shader shader = new LinearGradient(0, 0, 0, height, new int[] {
				getResources().getColor(android.R.color.holo_blue_bright),
				getResources().getColor(android.R.color.holo_blue_light),
				getResources().getColor(android.R.color.holo_blue_dark) },
				null, Shader.TileMode.REPEAT);
		paint.setShader(shader);
		canvas.drawRect(0, 0, width, height, paint);
		*/
        //绘制气泡
        paint.reset();
        paint.setColor(0X669999);//灰白色
        paint.setAlpha(45);//设置不透明度：透明为0，完全不透明为255
        List<Bubble> list = new ArrayList<Bubble>(bubbles);
        //依次绘制气泡
        for (Bubble bubble : list) {
            //碰到上边界从数组中移除
            Log.e("DoubleWaveView", "Bobe: height " + height * 8 / 10);
            if (bubble.getY() - height * 2 / 10 - dip2px(getContext(), 15) - bubble.getSpeedY() <= 0) {
                bubbles.remove(bubble);
            }
            //碰到左边界从数组中移除
            else if (bubble.getX() - bubble.getRadius() <= 0) {
                bubbles.remove(bubble);
            }
            //碰到右边界从数组中移除
            else if (bubble.getX() + bubble.getRadius() >= width) {
                bubbles.remove(bubble);
            } else {
                int i = bubbles.indexOf(bubble);
                if (bubble.getX() + bubble.getSpeedX() <= bubble.getRadius()) {
                    bubble.setX(bubble.getRadius());
                } else if (bubble.getX() + bubble.getSpeedX() >= width
                        - bubble.getRadius()) {
                    bubble.setX(width - bubble.getRadius());
                } else {
                    bubble.setX(bubble.getX() + bubble.getSpeedX());
                }
                bubble.setY(bubble.getY() - bubble.getSpeedY());

                //海底溢出的甲烷上升过程越来越大（气压减小）
                //鱼类和潜水员吐出的气体却会越变越小（被海水和藻类吸收）
                //如果考虑太多现实情景的话，代码量就会变得很大，也容易出现bug
                //感兴趣的读者可以自行添加
                //bubble.setRadius(bubble.getRadius());

                bubbles.set(i, bubble);
                canvas.drawCircle(bubble.getX(), bubble.getY(),
                        bubble.getRadius(), paint);
            }
        }
        //刷新屏幕
        invalidate();
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

    //内部VO，不需要太多注释吧？
    private class Bubble {
        //气泡半径
        private float radius;
        //上升速度
        private float speedY;
        //平移速度
        private float speedX;
        //气泡x坐标
        private float x;
        // 气泡y坐标
        private float y;

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
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

        public float getSpeedY() {
            return speedY;
        }

        public void setSpeedY(float speedY) {
            this.speedY = speedY;
        }

        public float getSpeedX() {
            return speedX;
        }

        public void setSpeedX(float speedX) {
            this.speedX = speedX;
        }

    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
