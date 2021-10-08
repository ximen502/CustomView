package com.example.demo2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class BezierView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaintArc;
    private Paint mPaint;
    private Paint mPaintPoint;
    private Paint mPaintFrame;
    private Paint mPaintLast;
    private boolean scanning = false;
    float per = 0f;
    Path path = new Path();


    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        int strokeWidth = Px.dip2px(getContext(), 1);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaintPoint = new Paint(mPaint);
        mPaintPoint.setStyle(Paint.Style.FILL);
        mPaintPoint.setColor(Color.parseColor("#4169e1"));
        //mPaint.setStrokeWidth(Px.dip2px(this.getContext(), 5));

        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArc.setColor(Color.BLUE);
        mPaintArc.setStyle(Paint.Style.FILL);

        mPaintFrame = new Paint(mPaint);
        mPaintFrame.setStrokeWidth(strokeWidth);
        mPaintFrame.setColor(Color.parseColor("#882222ff"));

        mPaintLast = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLast.setColor(Color.parseColor("#ff9912"));
        mPaintLast.setStrokeWidth(strokeWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = width;
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = height;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#dcdcdc"));

        //canvas.translate(mWidth/2, mHeight/2);

        float p0x,p0y;
        float p1x,p1y;
        float p2x,p2y;
        float p3x,p3y;
        float p4x,p4y;
        float p5x,p5y;

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        float off = 20;
        float startX = 20, startY = 20, stopX = mWidth - 20, stopY = 20;
        p0x=startX;
        p0y=startY;
        p1x=cx;
        p1y=mHeight-off;
        p2x=stopX;
        p2y=stopY;

        path.reset();
        path.moveTo(p0x, p0y);
        path.quadTo(p1x,p1y,p2x,p2y);
        canvas.drawPath(path, mPaint);

        // canvas.drawCircle(cx, cy, 30, mPaint);
        canvas.drawLine(p0x,p0y,p1x,p1y, mPaintFrame);
        canvas.drawLine(p1x,p1y,p2x,p2y, mPaintFrame);

        p3x=p0x+(p1x-p0x)*per;
        p3y=p0y+(p1y-p0y)*per;
        p4x=p1x+(p2x-p1x)*per;
        p4y=p1y-(p1y-p2y)*per;
        p5x=p3x+(p4x-p3x)*per;
        p5y=p3y+(p4y-p3y)*per;
        canvas.drawLine(p3x,p3y,p4x,p4y,mPaintLast);
        mPaintPoint.setColor(Color.CYAN);
        canvas.drawCircle(p3x,p3y, 10, mPaintPoint);
        canvas.drawCircle(p4x,p4y, 10, mPaintPoint);
        canvas.drawCircle(p5x,p5y, 10, mPaintPoint);

        mPaintPoint.setColor(Color.parseColor("#4169e1"));
        canvas.drawCircle(startX, startY, 10, mPaintPoint);
        canvas.drawCircle(cx, mHeight - off, 10, mPaintPoint);
        canvas.drawCircle(stopX, stopY, 10, mPaintPoint);
        per += 0.005f;
        if (per <= 1){
            invalidate();
        }else{
            per = 0f;
            invalidate();
        }
//        postInvalidateDelayed(50);
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }
}
