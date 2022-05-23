package com.exam.demo4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

/**
 * 仿H5的模拟时钟实现
 *
 */
public class AnalogClockView extends View {
    final int TEXT_SIZE = 14;
    final int DEFAULT_TEXT_COLOR = Color.argb(255, 0, 255, 70);
    final float THETA = (float) (2 * Math.PI / 60);

    Paint mPaint;
    int mWidth = 0;
    int mHeight = 0;
    float radius = 0;

    int secondHandColor = Color.parseColor("#f3a829");;
    int minuteHandColor = Color.parseColor("#222222");;
    int hourHandColor = Color.parseColor("#222222");;
    int markColor = Color.parseColor("#222222");
    int hour;
    int minute;
    int second;

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(markColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(getContext().getResources().getDisplayMetrics().density * TEXT_SIZE);

        //Log.i("ndkapp", getChar());

        // 阴影效果的参考：https://www.jianshu.com/p/2f1024f9c554
        setLayerType(LAYER_TYPE_SOFTWARE, null);

    }

    public AnalogClockView(Context context) {
        this(context, null);
    }

    public AnalogClockView(Context context, @Nullable  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClockView(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 自定义属性的参考： https://segmentfault.com/a/1190000040140904
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView,defStyleAttr, 0);
        secondHandColor = a.getColor(R.styleable.AnalogClockView_secondHandColor, DEFAULT_TEXT_COLOR);
        minuteHandColor = a.getColor(R.styleable.AnalogClockView_minuteHandColor, DEFAULT_TEXT_COLOR);
        hourHandColor = a.getColor(R.styleable.AnalogClockView_hourHandColor, DEFAULT_TEXT_COLOR);
        markColor = a.getColor(R.styleable.AnalogClockView_markColor, DEFAULT_TEXT_COLOR);
        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);
        radius = (mWidth > mHeight ? mHeight : mWidth) * 0.5f * 0.8f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        drawMarks(canvas);

        String brand = "北极星";
        float bw = mPaint.measureText(brand);
        canvas.drawText(brand, 0 - bw / 2, radius * 0.4f, mPaint);
        String brand2 = "亚洲";
        float textSize = mPaint.getTextSize();
        mPaint.setTextSize(textSize * 0.7f);
        float bw2 = mPaint.measureText(brand2);
        canvas.drawText(brand2, 0 - bw2 / 2, radius * 0.4f + mPaint.getTextSize() + 5, mPaint);
        mPaint.setTextSize(textSize);

        float x1 = 0f;
        float y1 = 0f;
        float x2 = 0f;
        float y2 = 0f;

        float hourTheta = (float) (2 * Math.PI / 12);
        //draw hour hand
        mPaint.setStrokeWidth(20);
        mPaint.setColor(hourHandColor);
        mPaint.setShadowLayer(9, 1, 1, Color.argb(0x88, 0,0,0));
        /* hourTheta * (minute / 60f)分针变化对时针的影响 2pi/12*(minute/60) */
        x2 = (float) (radius * 0.75f * Math.sin(hourTheta * (hour + minute / 60f)));
        y2 = -(float) (radius * 0.75f * Math.cos(hourTheta * (hour + minute / 60f)));
        canvas.drawLine(x1, y1, x2, y2, mPaint);

        //draw minute hand
        mPaint.setStrokeWidth(9);
        mPaint.setColor(minuteHandColor);
        /* theta * (second / 60f)秒针变化对分针的影响 //2pi/60 * (second/60)*/
        x2 = (float) (radius * 1.05f * Math.sin(THETA * (minute + second / 60f)));
        y2 = -(float) (radius * 1.05f * Math.cos(THETA * (minute + second / 60f)));
        canvas.drawLine(x1, y1, x2, y2, mPaint);

        //draw second hand
        mPaint.setStrokeWidth(3);
        mPaint.setColor(secondHandColor);
        x2 = (float) (radius * 1.1f * Math.sin(THETA * second));
        y2 = -(float) (radius * 1.1f * Math.cos(THETA * second));
        canvas.drawLine(x1, y1, x2, y2, mPaint);

        //draw second tail handle
        mPaint.setStrokeWidth(13);
        mPaint.setColor(secondHandColor);
        x2 = (float) (radius * 0.1f * Math.sin(THETA * second));
        y2 = -(float) (radius * 0.1f * Math.cos(THETA * second));
        canvas.drawLine(x1, y1, -x2, -y2, mPaint);

        // draw circle cover three hands
        float worh = 16f;
        canvas.drawCircle(0, 0, worh, mPaint);
        mPaint.clearShadowLayer();
    }

    private void drawMarks(Canvas canvas) {
        mPaint.setColor(markColor);
        for (int i = 0; i < 60; i++) {
            float x1 = 0f;
            float y1 = 0f;
            float x2 = 0f;
            float y2 = 0f;
            float nx = 0f;
            float ny = 0f;
            float sint = (float) Math.sin(THETA * i);
            float cost = (float) Math.cos(THETA * i);
            if (i % 5 == 0) {
                x1 = (float) (sint * radius);
                y1 = -(float) (cost * radius);
                x2 = (float) (sint * radius * 1.12);
                y2 = -(float) (cost * radius * 1.12);
                nx = (float) (sint * radius * 0.85);
                ny = -(float) (cost * radius * 0.85);
                mPaint.setStrokeWidth(10);
                String num = String.valueOf(i / 5 == 0 ? 12 : i / 5);
                float numW = mPaint.measureText(num);
                canvas.drawText(num, nx - numW * 0.5f, ny + mPaint.getTextSize() / 2, mPaint);
            } else {
                x1 = (float) (sint * (radius * 1.05));
                y1 = -(float) (cost * (radius * 1.05));
                x2 = (float) (sint * radius * 1.12);
                y2 = -(float) (cost * radius * 1.12);
                mPaint.setStrokeWidth(5);
            }
            canvas.drawLine(x1, y1, x2, y2, mPaint);
        }
    }

    private void setCurrentTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
//        setCurrentTime(2, 22, 30);
//        CountDownTimer timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Calendar calendar = Calendar.getInstance();
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//                int second = calendar.get(Calendar.SECOND);
//                setCurrentTime(hour % 12, minute, second);
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };
//
//        timer.start();

    }

    /**
     * 外部调用动态更新表盘
     */
    public void start() {
        new Starter().start();
    }

    class Starter extends Thread {
        @Override
        public void run() {
            super.run();
            while(true) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                setCurrentTime(hour % 12, minute, second);
                postInvalidate();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
