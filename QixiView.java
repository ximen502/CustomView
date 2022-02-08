package com.bzu.digitalrain.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bzu.digitalrain.R;

public class QixiView extends View {

    Paint paint;
    int w, h;
    Bitmap flower;
    Canvas hearCanvas;
    Bitmap bmpHeart;
    float leftAngle = (float)( 2*Math.PI);
    float rightAngle = 0;
    double inc = Math.PI / 45;
    int zoom = 1;
    float finalTxSize;

    public QixiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        float density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#FFDB9C"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(/*density*22*/0);
        paint.setStrokeWidth(2);
        paint.setDither(true);

        flower = BitmapFactory.decodeResource(getResources(), R.mipmap.heart);

        finalTxSize = density * 22;
    }

    public QixiView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wM = MeasureSpec.getMode(widthMeasureSpec);
        int hM = MeasureSpec.getMode(heightMeasureSpec);
        if (wM == MeasureSpec.EXACTLY) {
            w = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (hM == MeasureSpec.EXACTLY) {
            h = MeasureSpec.getSize(heightMeasureSpec);
        }

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        hearCanvas = new Canvas();
        bmpHeart = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        hearCanvas.setBitmap(bmpHeart);
        // 计算需要放大的倍数，铺满屏幕宽度
        zoom = (int) Math.ceil(w / 2.0 / 16.0 - 4);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHeart(canvas);
        canvas.drawBitmap(bmpHeart, 0, 0, paint);

        if (rightAngle > Math.PI || leftAngle < Math.PI){
            Log.d("xsc", "draw hear ok");
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("情人节快乐", w/2, h/2, paint);
            paint.setTextAlign(Paint.Align.LEFT);

            if (paint.getTextSize() <= finalTxSize) {
                paint.setTextSize(paint.getTextSize()+1);
                postInvalidateDelayed(25);
            }else{
                Log.d("xsc", "draw txt ok");
            }
        }
    }

    public void redraw(){
        leftAngle = (float)( 2*Math.PI);
        rightAngle = 0;
        paint.setTextSize(0);
        hearCanvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    private void drawHeart(Canvas canvas) {
        hearCanvas.save();
        hearCanvas.translate(w / 2, h / 2);//
        ////////////////////////////////
        float leftX = (float) getX(zoom, leftAngle);
        float leftY = (float) getY(zoom, leftAngle);
        float rightX = (float) getX(zoom, rightAngle);
        float rightY = (float) getY(zoom, rightAngle);
        //Log.d("xsc", String.format("x:%s, y:%s", x, y));
        hearCanvas.save();
        hearCanvas.translate(-flower.getWidth()/2, -flower.getHeight()/2);
        hearCanvas.drawBitmap(flower, leftX, leftY, paint);
        hearCanvas.drawBitmap(flower, rightX, rightY, paint);
        hearCanvas.restore();
        hearCanvas.restore();
        ////////////////////////////////

        // right half
        if(rightAngle <= Math.PI){
            rightAngle += inc;
            //postInvalidateDelayed(50);
        }

        // left half
        if(leftAngle >= Math.PI){
            leftAngle -= inc;
            postInvalidateDelayed(50);
        }
    }

    double getX(int zoom, float theta) {
        return zoom * (16 * Math.pow(Math.sin(theta), 3));
    }

    double getY(int zoom, float theta) {
        return -zoom
                * (13 * Math.cos(theta) - 5 * Math.cos(2 * theta) - 2
                * Math.cos(3 * theta) - Math.cos(4 * theta));
    }

    protected void paint(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        String text = "\frac{2}{3}";
        int x = 50;
        int y = 30;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        paint.setStrokeWidth(1);
        paint.setDither(true);
        text = "2";
        canvas.drawText(text, x, y, paint);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        System.out.println(bounds.left);
        System.out.println(bounds.top);
        System.out.println(bounds.right);
        System.out.println(bounds.bottom);

        float w = paint.measureText(text);

        System.out.println("w:" + w);

        canvas.drawLine(x - 10, y + 10, x + 20, y + 10, paint);

        text = "3";
        canvas.drawText(text, x, y + 30, paint);

        text = "2";
        canvas.drawText(text, x + 30, y + 30, paint);

        paint.setTextSize(10);
        text = "3";
        canvas.drawText(text, x + 40, y + 20, paint);

    }

    // 保存到本地
    public void SaveBitmap(Bitmap bmp) {
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        paint(canvas);

        // 加载背景图片
        // Bitmap bmps = BitmapFactory.decodeResource(getResources(),
        // R.drawable.and);
        // canvas.drawBitmap(bmps, 0, 0, null);
        // 加载要保存的画面
        // canvas.drawBitmap(bmp, 10, 100, null);
        // 保存全部图层
        // canvas.save(Canvas.ALL_SAVE_FLAG);
        // canvas.restore();
        // 存储路径
        // File file = new File("/sdcard/gkbb/");
        // if(!file.exists())
        // file.mkdirs();
        // try {
        // FileOutputStream fileOutputStream = new
        // FileOutputStream(file.getPath() + "/xuanzhuan.jpg");
        // bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        // fileOutputStream.close();
        // System.out.println("saveBmp is here");
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

}
