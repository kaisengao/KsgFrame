package com.kaisengao.ksgframe.common.fish.anim.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @ClassName: SurfaceFish4Anim
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/3 10:17
 * @Description: 辅鱼（背景鱼）
 */
public abstract class BaseAuxiliaryFishAnim extends BaseSurfaceAnim implements ValueAnimator.AnimatorUpdateListener {

    /**
     * 画布起点
     */
    private float mMoveX, mMoveY;
    /**
     * 三阶贝赛尔 控制点1
     */
    private float mX1, mY1;
    /**
     * 三阶贝赛尔 控制点2
     */
    private float mX2, mY2;
    /**
     * 三阶贝赛尔 结束点
     */
    private float mEndX, mEndY;
    /**
     * 实时路径
     */
    private float mCurAnimValue;

    private Path mDstPath, mValuePath;

    private PathMeasure mPathMeasure;

    private ValueAnimator mPathAnimator;

    private final Matrix mMatrix = new Matrix();

    public BaseAuxiliaryFishAnim(Context context) {
        super(context);
    }

    /**
     * Init
     */
    @Override
    protected void init() {
        super.init();
        // Path
        this.mDstPath = new Path();
        this.mValuePath = new Path();
        // 属性动画
        this.mPathAnimator = ValueAnimator.ofFloat(0, 1);
        this.mPathAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mPathAnimator.addUpdateListener(this);
        this.mPathAnimator.setDuration(25 * 1000);
    }

    /**
     * 动画更新监听
     */
    public void onAnimationUpdate(ValueAnimator animator) {
        // 实时路径
        this.mCurAnimValue = (float) animator.getAnimatedValue();
    }

    /**
     * 绘制
     */
    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (!mPathAnimator.isStarted()) {
            this.generatePathAnimation();
        } else if (mPathAnimator.isPaused()) {
            this.mPathAnimator.resume();
        }
        // 计算路径长度 与 当前路线
        float length = mPathMeasure.getLength();
        float stop = length * mCurAnimValue;
        this.mDstPath.reset();
        // 截取片段
        this.mPathMeasure.getSegment(0, stop, mDstPath, true);
        // 将pos信息和tan信息保存在mMatrix中
        this.mPathMeasure.getMatrix(stop, mMatrix, PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        // 将图片的旋转坐标调整到图片中心位置
        this.mMatrix.preTranslate(-mFrameWidth >> 1, -mFrameHeight >> 1);
        // 绘制帧图
        canvas.drawBitmap(getFrameBitmap(), mMatrix, paint);
    }

    /**
     * 路径动画
     */
    private void generatePathAnimation() {
        // 控制点
        this.randomPoint(true);
        this.randomControlPoint();
        this.randomPoint(false);
        // 重置Path
        this.mDstPath.reset();
        this.mValuePath.reset();
        // 画轨迹线
        this.mValuePath.moveTo(mMoveX, mMoveY);
        this.mValuePath.cubicTo(mX1, mY1, mX2, mY2, mEndX, mEndY);
        // 轨迹动画
        this.mPathMeasure = new PathMeasure(mValuePath, false);
        // 路径动画
        this.mPathAnimator.start();
    }

    /**
     * 随机起点/终点坐标
     *
     * @param startPoint 起点
     */
    private void randomPoint(boolean startPoint) {
        float randomX, randomY;
        switch (mRandom.nextInt(4)) {
            case 1:
                // 上
                randomX = mRandom.nextInt(mViewWidth);
                randomY = -mFrameHeight;
                break;
            case 2:
                // 下
                randomX = mRandom.nextInt(mViewWidth);
                randomY = mViewHeight + mFrameHeight;
                break;
            case 3:
                // 左
                randomX = -mFrameWidth;
                randomY = mRandom.nextInt(mViewHeight);
                break;
            default:
                // 右
                randomX = mViewWidth + mFrameWidth;
                randomY = mRandom.nextInt(mViewHeight);
                break;
        }
        // 更新坐标
        if (startPoint) {
            this.mMoveX = randomX;
            this.mMoveY = randomY;
        } else {
            this.mEndX = randomX;
            this.mEndY = randomY;
        }
    }

    /**
     * 随机控制点坐标
     */
    private void randomControlPoint() {
        this.mX1 = mRandom.nextInt(mViewWidth);
        this.mY1 = mRandom.nextInt(mViewHeight);
        this.mX2 = mRandom.nextInt(mViewWidth);
        this.mY2 = mRandom.nextInt(mViewHeight);
    }

    /**
     * 暂停 路径动画
     */
    private void pausePathAnimation() {
        if (mPathAnimator != null) {
            this.mPathAnimator.pause();
        }
    }

    /**
     * 停止 路径动画
     */
    private void stopPathAnimation() {
        if (mPathAnimator != null) {
            this.mPathAnimator.removeAllListeners();
            this.mPathAnimator.cancel();
            this.mPathAnimator = null;
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        this.pausePathAnimation();
    }

    /**
     * 释放
     */
    @Override
    public void release() {
        super.release();
        // 停止 路径动画
        this.stopPathAnimation();
    }
}