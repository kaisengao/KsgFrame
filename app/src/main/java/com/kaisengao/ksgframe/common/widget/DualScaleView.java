package com.kaisengao.ksgframe.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ClassName: DualScaleView
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/17 11:53
 * @Description: 双屏调整比例View
 */
public class DualScaleView extends View {

    private int mViewWidth, mViewHeight;

    private float mStartY;

    private float mSplitRatio = 0;

    private String mUpText = "1号屏", mDownText = "2号屏";

    private Paint mPaint;

    private TextPaint mTextPaint;

    private final Rect mTextRect = new Rect();

    public DualScaleView(Context context) {
        this(context, null);
    }

    public DualScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        // 画笔
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setStrokeWidth(3);
        // Text画笔
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setColor(Color.BLACK);
        this.mTextPaint.setTextSize(60);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mViewWidth = w;
        this.mViewHeight = h;
        this.mSplitRatio = mViewHeight * 0.7f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, mViewWidth, mSplitRatio, mPaint);

        this.mPaint.setColor(Color.GREEN);
        canvas.drawRect(0, mSplitRatio, mViewWidth, mViewHeight, mPaint);

        this.mPaint.setColor(Color.WHITE);
        canvas.drawLine(0, mSplitRatio, mViewWidth, mSplitRatio, mPaint);

        this.mTextPaint.getTextBounds(mUpText, 0, mUpText.length(), mTextRect);
        canvas.drawText(mUpText, (mViewWidth >> 1) - (mTextRect.width() >> 1),
                mSplitRatio / 2 + (mTextRect.height()) - ((mTextRect.height()) >> 1), mTextPaint);

        this.mTextPaint.getTextBounds(mDownText, 0, mDownText.length(), mTextRect);
        canvas.drawText(mDownText, ((mViewWidth >> 1) - (mTextRect.width() >> 1)),
                mViewHeight - (mViewHeight - mSplitRatio) / 2 + (mTextRect.height()) - ((mTextRect.height()) >> 1), mTextPaint);
    }

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                // 计算偏移量
                int onePercent = mViewHeight / 100;
                if (Math.abs(moveY - mStartY) >= onePercent) {
                    if (moveY > mStartY) {
                        this.mSplitRatio += onePercent;
                    } else {
                        this.mSplitRatio -= onePercent;
                    }
                    // 高度围栏
                    if (mSplitRatio >= mViewHeight) {
                        this.mSplitRatio = mViewHeight;
                    } else if (mSplitRatio <= 0) {
                        this.mSplitRatio = 0;
                    }
                    // 百分比文字
                    float percent = ((mSplitRatio / getHeight()) * 100);
                    this.mUpText = String.format("%.0f%%", percent);
                    this.mDownText = String.format("%.0f%%", 100 - percent);
                    // 更新初始坐标
                    this.mStartY = moveY;
                    // 重绘
                    this.invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }
}