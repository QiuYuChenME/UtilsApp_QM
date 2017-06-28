package com.qm.customview_qmlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.PrivateKey;

/**
 * Created by XueYang on 2017/6/27.
 */

public class TopBar extends RelativeLayout {
    private Button right_btn,left_btn;
    private TextView title_tv;

    private float titleTextSize;
    private int titleTextColor;
    private String title;

    private String leftText;

    private String rightText;

    private LayoutParams leftParams,rightParams,centerParams;
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.TopBar);

        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor,0);
        title = ta.getString(R.styleable.TopBar_title);
        titleTextSize =ta.getDimension(R.styleable.TopBar_titleTextSize,0);

        leftText = ta.getString(R.styleable.TopBar_leftText);

        rightText = ta.getString(R.styleable.TopBar_rightText);

        ta.recycle();

        left_btn = new Button(context);
        left_btn.setText(leftText);

        right_btn = new Button(context);
        right_btn.setText(rightText);

        title_tv = new TextView(context);
        title_tv.setBackgroundColor(titleTextColor);
        title_tv.setText(title);
        title_tv.setGravity(Gravity.CENTER);

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(left_btn,leftParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(right_btn,rightParams);
        setBackgroundColor(0XFFF59563);

        centerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        centerParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(title_tv,centerParams);
    }

}
