package com.kaisengao.ksgframe.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.HandlerThread;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kaisengao.base.configure.WeakHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @version v1.0 2022年03月09日 此版本暂不支持文字与头像文字同时存在
 * 例：纯文字祈福 or 头像加文字祈福 只能存在一个
 * @ClassName: BlessingView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/8 13:29
 * @Description: 祈福View
 */
public class BlessingView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "BlessingView";

    private static final int LR_SPACING = 120;

    private static final int TB_SPACING = 30;

    private static final float SPEED = 2.5f;

    private int mViewWidth, mViewHeight;

    private int mTBSpacing = TB_SPACING;

    private boolean isAlive;

    private Canvas mCanvas;

    private Paint mBitPaint;

    private TextPaint mTextPaint;

    private PaintFlagsDrawFilter mDrawPaint;

    private ConcurrentLinkedQueue<BlessingBean> mDrawQueue;

    private ConcurrentLinkedQueue<BlessingBean> mWaitQueue;

    private WeakHandler mHandler;

    private HandlerThread mHandlerThread;

    private SurfaceHolder mSurfaceHolder;

    private final Rect mTextRect = new Rect();

    public BlessingView(Context context) {
        this(context, null);
    }

    public BlessingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    private void init() {
        // 队列
        this.mDrawQueue = new ConcurrentLinkedQueue<>();
        this.mWaitQueue = new ConcurrentLinkedQueue<>();
        // SurfaceHolder
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        // 设置透明背景，否则SurfaceView背景是黑的
        this.mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        // 如果想让SurfaceView显示图片或者视频必须要调用
        this.setZOrderOnTop(true);
        // 画笔
        this.mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mBitPaint.setFilterBitmap(true);
        this.mBitPaint.setAntiAlias(true);
        this.mBitPaint.setDither(true);
        // Text画笔
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        this.mTextPaint.setTextSize(20);
        // 抗锯齿
        this.mDrawPaint = new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    /**
     * 设置 字体颜色
     */
    public void setTextColor(@ColorRes int color) {
        this.mTextPaint.setColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * 设置 字体大小
     */
    public void setTextSize(float textSize) {
        this.mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize
                , getResources().getDisplayMetrics()));
    }

    /**
     * 设置 上下间距
     */
    public void setTBSpacing(int TBSpacing) {
        this.mTBSpacing = TBSpacing;
    }

    /**
     * 发送 祝福
     *
     * @param text 祝福语句
     */
    public void sendBlessing(String text) {
        this.sendBlessing(null, text);
    }

    /**
     * 发送 祝福
     *
     * @param avatar 头像
     * @param text   祝福语句
     */
    public void sendBlessing(@Nullable Bitmap avatar, String text) {
        // 测量文字宽高
        this.mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
        // 创建实例
        BlessingBean blessing = new BlessingBean();
        // Text
        blessing.textWidth = mTextRect.width();
        blessing.textHeight = mTextRect.height();
        blessing.text = text;
        // Avatar
        if (avatar != null) {
            blessing.avatarWidth = avatar.getWidth();
            blessing.avatarHeight = avatar.getHeight();
            blessing.avatar = avatar;
        }
        // X Y
        blessing.x = mViewWidth;
        // 所有Item的高度总和
        int totalHeight = 0;

        // 计算初始Y坐标
        if (!mDrawQueue.isEmpty()) {
            for (BlessingBean item : mDrawQueue) {
                if (item.avatar != null) {
                    totalHeight += item.avatarHeight;
                } else {
                    totalHeight += item.textHeight + mTBSpacing;
                }
            }
            // Item高度
            float itemHeight = blessing.avatar != null ? blessing.avatarHeight : blessing.textHeight;
            // 判断当前的画布高度是否还可以插入
            if ((mViewHeight - totalHeight) < itemHeight) {
                // 没有空间了则加入等待队列
                this.mWaitQueue.add(blessing);
                return;
            }
        }
        // Y坐标
        blessing.y = totalHeight + (blessing.avatar != null ? 0 : blessing.textHeight);
        // 标记存活
        blessing.isAlive = true;
        // 加入队列
        this.mDrawQueue.add(blessing);
        // 启动线程
        if (!isAlive) {
            this.startDrawThread();
        }
    }

    /**
     * Surface 创建
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "----surfaceCreated-----");
    }

    /**
     * Surface 改变
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "----surfaceChanged----- width = " + width + ", height = " + height);
        this.mViewWidth = width;
        this.mViewHeight = height;
    }

    /**
     * Surface 销毁
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "----surfaceDestroyed-----");
    }

    /**
     * 启动线程
     */
    private void startDrawThread() {
        // 标记存活
        this.isAlive = true;
        // 启动
        this.mHandlerThread = new HandlerThread("BlessingViewThread");
        this.mHandlerThread.start();
        this.mHandler = new WeakHandler(mHandlerThread.getLooper());
        this.mHandler.post(this);
    }

    /**
     * 绘制线程
     */
    @Override
    public void run() {
        if (!isAlive) {
            return;
        }
        // Draw
        try {
            this.mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas != null) {
                this.mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                this.mCanvas.setDrawFilter(mDrawPaint);
                // 检测
                if (!mDrawQueue.isEmpty()) {
                    List<BlessingBean> blessings = new ArrayList<>();
                    // 遍历
                    Iterator<BlessingBean> iterator = mDrawQueue.iterator();
                    while (iterator.hasNext()) {
                        BlessingBean next = iterator.next();
                        if (next.isFollow && !next.hasFollow && !mWaitQueue.isEmpty()) {
                            // 标记已经被追加
                            next.hasFollow = true;
                            // 从队列中取一条
                            BlessingBean blessing = mWaitQueue.poll();
                            if (blessing != null) {
                                // 直取上一条的Y坐标即可
                                blessing.y = next.y;
                                // 标记存活
                                blessing.isAlive = true;
                                // 插入集合
                                blessings.add(blessing);
                            }
                        }
                        if (!next.isAlive) {
                            iterator.remove();
                        }
                    }
                    // 加入队列
                    if (!blessings.isEmpty()) {
                        this.mDrawQueue.addAll(blessings);
                    }
                }
                // 绘制
                for (BlessingBean item : mDrawQueue) {
                    // X - 速度 = 距离
                    item.x = item.x - SPEED;
                    float textX = item.x;
                    float textY = item.y;
                    float itemWidth = item.textWidth;
                    // 头像
                    if (item.avatar != null) {
                        this.mCanvas.drawBitmap(item.avatar, item.x, item.y, mBitPaint);
                        // 计算Text的坐标
                        textX = (item.x + item.avatarWidth + 15);
                        textY = ((item.y + (item.avatarHeight >> 1)) + (item.textHeight >> 1));
                        // 头像 + 文字 = 总宽度
                        itemWidth = item.avatarWidth + 15 + item.textWidth;
                    }
                    // 文字
                    this.mCanvas.drawText(item.text, textX, textY, mTextPaint);
                    // 验证尾部是否可以追加
                    if ((mViewWidth - item.x - itemWidth) > LR_SPACING && !item.hasFollow) {
                        item.isFollow = true;
                    }
                    // 验证是否已经滚出画布
                    if ((item.x + itemWidth) <= 0) {
                        item.isAlive = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception = " + e);
        } finally {
            if (mCanvas != null) {
                this.mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
        // 刷新绘制
        if (mHandler != null) {
            this.mHandler.postDelayed(this, 5);
        }
    }

    /**
     * 停止线程
     */
    private void stopDrawThread() {
        // 标记销毁
        this.isAlive = false;
        // 停止
        if (mHandlerThread != null) {
            this.mHandlerThread.quit();
            this.mHandlerThread = null;
        }
        if (mHandler != null) {
            this.mHandler.removeCallbacks(this);
            this.mHandler = null;
        }
    }

    /**
     * 释放
     */
    public void release() {
        // Remove
        this.mSurfaceHolder.removeCallback(this);
        // 释放
        this.mDrawQueue.clear();
        this.mDrawQueue = null;
        this.mWaitQueue.clear();
        this.mWaitQueue = null;
        // 停止线程
        this.stopDrawThread();
    }

    /**
     * onDetached
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 释放资源
        this.release();
    }

    static class BlessingBean {

        int textWidth, textHeight;

        int avatarWidth, avatarHeight;

        float x, y;

        boolean isAlive = false;

        boolean isFollow = false;

        boolean hasFollow = false;

        String text;

        Bitmap avatar;
    }
}