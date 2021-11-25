package com.kasiengao.ksgframe.java.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ClassName: CanvasKsgView
 * @Author: KaiSenGao
 * @CreateDate: 2021/10/25 10:32
 * @Description: 绘制KSG文字
 */
public class CanvasKsgView extends View {

    private Path mPath;

    private Paint mPaint;
    private Paint mTextPaint;

    public CanvasKsgView(Context context) {
        this(context, null);
    }

    public CanvasKsgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        this.mPath = new Path();

        this.mPaint = new Paint();
        this.mPaint.setStrokeWidth(10.0f);
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setStyle(Paint.Style.STROKE);

        this.mTextPaint = new Paint();
        this.mTextPaint.setTextSize(100);
        this.mTextPaint.setColor(Color.BLACK);
        this.mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("KSG", 50, 100, mTextPaint);

        this.mPath.moveTo(100, 500);
//        this.mPath.quadTo(200, 100, 600, 500);
//        this.mPath.cubicTo(100, 500, 140, 100, 200, 500);


//        this.mPath.lineTo(200, 500);

        //画矩形
//        canvas.drawRect(rectF,mPaint);

        this.mPath.arcTo(340,600,740,1000,0,90,true);

        this.mPath.lineTo(340, 740);
        this.mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}