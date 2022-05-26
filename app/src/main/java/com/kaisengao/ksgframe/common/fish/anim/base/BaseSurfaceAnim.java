package com.kaisengao.ksgframe.common.fish.anim.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import java.util.Random;

/**
 * @ClassName: BaseSurfaceAnim
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/2 13:56
 * @Description:
 */
public abstract class BaseSurfaceAnim implements ISurfaceAnim {

    private static final int DEFAULT_FRAME_DURATION = 50; // ms

    protected Context mContext;

    protected Random mRandom = new Random();

    protected int mViewWidth, mViewHeight;

    protected int mFrameWidth, mFrameHeight;

    private int mCacheIndex;

    private int mCacheTotal;

    private long mLastTimeMillis = 0;

    private long mFrameDuration = DEFAULT_FRAME_DURATION;

    private boolean isAnimEnd;

    private boolean isRelease;

    private Bitmap mMaskBitmap;

    private SparseArray<Bitmap> mBitmapCaches;

    public BaseSurfaceAnim(Context context) {
        this.mContext = context;
        // Init
        this.init();
    }

    /**
     * Init
     */
    protected void init() {
        // 初始
        this.setAnimEnd(false);
    }

    public void setMaskBitmap(Bitmap maskBitmap) {
        this.mMaskBitmap = maskBitmap;
    }

    /**
     * RootView
     *
     * @param viewWidth  int
     * @param viewHeight int
     */
    @Override
    public void setRootViewWH(int viewWidth, int viewHeight) {
        this.mViewWidth = viewWidth;
        this.mViewHeight = viewHeight;
    }

    /**
     * 获取 蒙版 Bitmap
     *
     * @return Bitmap
     */
    public Bitmap getMaskBitmap() {
        return mMaskBitmap;
    }

    /**
     * 设置 帧动画间隔
     *
     * @param frameDuration frameDuration
     */
    protected void setFrameDuration(long frameDuration) {
        this.mFrameDuration = frameDuration;
    }

    /**
     * 获取 帧动画间隔
     *
     * @return frameDuration
     */
    protected long getFrameDuration() {
        return mFrameDuration;
    }

    /**
     * 设置 动画播放结束状态
     *
     * @param animEnd boolean
     */
    protected void setAnimEnd(boolean animEnd) {
        this.isAnimEnd = animEnd;
    }

    /**
     * 动画结束标识
     */
    @Override
    public boolean isAnimEnd() {
        return isAnimEnd;
    }

    /**
     * 解码图片并缓存
     */
    protected void decodeBitmap(SparseArray<Bitmap> bitmapCaches) {
        this.mBitmapCaches = bitmapCaches;
        // 获取第一张图片的宽高
        Bitmap bitmap = mBitmapCaches.get(0);
        this.mFrameWidth = bitmap.getWidth();
        this.mFrameHeight = bitmap.getHeight();
        // 初始
        this.mCacheTotal = mBitmapCaches.size();
        this.mCacheIndex = mRandom.nextInt(mCacheTotal);
    }

    /**
     * 获取帧图
     *
     * @return Bitmap
     */
    protected Bitmap getFrameBitmap() {
        if (mCacheIndex >= mCacheTotal) {
            this.mCacheIndex = 0;
        }
        Bitmap frameBitmap = mBitmapCaches.get(mCacheIndex);
        // 获取帧图的时间戳
        long currentTimeMillis = System.currentTimeMillis();
        // 比对绘制时间是否达到了间隔时间 (达到或超过了就切换到下一张帧图)
        if ((currentTimeMillis - mLastTimeMillis) >= getFrameDuration()) {
            this.mCacheIndex++;
            this.mLastTimeMillis = currentTimeMillis;
        }
        // Return
        return frameBitmap;
    }

    /**
     * 是否已释放
     *
     * @return boolean
     */
    public boolean isRelease() {
        return isRelease;
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        this.isRelease = true;
        this.mContext = null;
        this.mRandom = null;
        this.mMaskBitmap = null;
    }
}