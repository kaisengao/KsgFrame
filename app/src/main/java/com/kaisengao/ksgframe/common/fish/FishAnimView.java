package com.kaisengao.ksgframe.common.fish;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kaisengao.base.configure.WeakHandler;
import com.kaisengao.ksgframe.common.fish.anim.base.BaseSurfaceAnim;
import com.kaisengao.ksgframe.common.fish.anim.base.ISurfaceAnim;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName: FishAnimView
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/2 10:44
 * @Description: 摸鱼儿
 */
public class FishAnimView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "FishAnimView";

    private int mViewWidth, mViewHeight;

    private boolean isAlive;

    private ConcurrentLinkedQueue<ISurfaceAnim> mAnimQueue;

    private Canvas mCanvas;

    private Paint mBitPaint;

    private PaintFlagsDrawFilter mDrawPaint;

    private WeakHandler mHandler;

    private HandlerThread mHandlerThread;

    private SurfaceHolder mSurfaceHolder;

    public FishAnimView(Context context) {
        this(context, null);
    }

    public FishAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        int[] screenSize = getScreenSize(getContext());
        this.mViewWidth = screenSize[0];
        this.mViewHeight = screenSize[1];
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
        // 抗锯齿
        this.mDrawPaint = new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        // Items
        this.mAnimQueue = new ConcurrentLinkedQueue<>();
    }


    public void addItem(BaseSurfaceAnim animBean) {
        animBean.setRootViewWH(mViewWidth, mViewHeight);
        // 添加到队列
        this.mAnimQueue.offer(animBean);
    }

    /**
     * Surface 创建
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "----surfaceCreated-----");
        // 标记存活
        this.isAlive = true;
        // 启动线程
        this.startDrawThread();
    }

    /**
     * Surface 改变
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mViewWidth = width;
        this.mViewHeight = height;
        // 更新View的宽高
        for (ISurfaceAnim surfaceAnim : mAnimQueue) {
            surfaceAnim.setRootViewWH(width, height);
        }
    }

    /**
     * Surface 销毁
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "----surfaceDestroyed-----");
        // 标记销毁
        this.isAlive = false;
    }

    /**
     * 绘制线程
     */
    @Override
    public void run() {
        if (!isAlive) {
            // 停止线程
            this.stopDrawThread();
            // 暂停动画 （pause、resume、start必须在同一线程)
            for (ISurfaceAnim surfaceAnim : mAnimQueue) {
                surfaceAnim.pause();
            }
            return;
        }
        // Draw
        try {
            this.mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas != null) {
                this.mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                this.mCanvas.setDrawFilter(mDrawPaint);
                // 遍历绘制
                Iterator<ISurfaceAnim> iterator = mAnimQueue.iterator();
                while (iterator.hasNext()) {
                    ISurfaceAnim next = iterator.next();
                    if (next.isAnimEnd()) {
                        next.release();
                        iterator.remove();
                    } else {
                        next.draw(mCanvas, mBitPaint);
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
     * 启动线程
     */
    private void startDrawThread() {
        if (mHandlerThread == null) {
            this.mHandlerThread = new HandlerThread("SurfaceViewThread");
            this.mHandlerThread.start();
        }
        if (mHandler == null) {
            this.mHandler = new WeakHandler(mHandlerThread.getLooper());
        }
        this.mHandler.post(this);
    }

    /**
     * 停止线程
     */
    private void stopDrawThread() {
        if (mHandler != null) {
            this.mHandler.removeCallbacks(this);
        }
    }

    /**
     * 释放线程
     */
    private void releaseDrawThread() {
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
        // 销毁资源
        for (ISurfaceAnim surfaceAnim : mAnimQueue) {
            surfaceAnim.release();
        }
        this.mAnimQueue.clear();
        this.mAnimQueue = null;
        // 释放线程
        this.releaseDrawThread();

        this.setVisibility(GONE);
    }

    /**
     * 获取屏幕真实分辨率
     *
     * @param context context
     * @return int[宽，高]
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }
}