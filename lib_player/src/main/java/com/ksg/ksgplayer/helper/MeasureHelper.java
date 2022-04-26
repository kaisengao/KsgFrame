package com.ksg.ksgplayer.helper;

import android.view.View;

import com.ksg.ksgplayer.config.AspectRatio;

/**
 * @ClassName: MeasureHelper
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/21 13:45
 * @Description: 视图宽高比算法
 */
public class MeasureHelper {

    private int mVideoWidth;
    private int mVideoHeight;
    private int mVideoSarNum;
    private int mVideoSarDen;

    private int mRotationDegree;

    private int mMeasuredWidth;
    private int mMeasuredHeight;

    private int mAspectRatio = AspectRatio.RATIO_DEFAULT;

    private int mCustomAspectRatio = 0;

    /**
     * 设置 画面宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
    }

    /**
     * 设置 视频旋转角度
     *
     * @param degree 角度
     */
    public void setRotationDegrees(int degree) {
        this.mRotationDegree = degree;
    }

    /**
     * 设置 视频采样率
     *
     * @param videoSarNum videoSarNum
     * @param videoSarDen videoSarDen
     */
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        this.mVideoSarNum = videoSarNum;
        this.mVideoSarDen = videoSarDen;
    }

    /**
     * 设置 画面比例
     *
     * @param aspectRatio {@link AspectRatio}
     */
    public void setAspectRatio(int aspectRatio) {
        this.mAspectRatio = aspectRatio;
    }

    /**
     * 设置 自定义画面比例
     */
    public void setCustomAspectRatio(int customAspectRatio) {
        this.mCustomAspectRatio = customAspectRatio;
    }

    /**
     * Must be called by View.onMeasure(int, int)
     *
     * @param widthMeasureSpec  widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    public void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mVideoHeight == 0 || mVideoWidth == 0) {
            this.mMeasuredWidth = 1;
            this. mMeasuredHeight = 1;
            return;
        }

        if (mRotationDegree == 90 || mRotationDegree == 270) {
            int tempSpec = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = tempSpec;
        }

        int realWidth = mVideoWidth;

        if (mVideoSarNum != 0 && mVideoSarDen != 0) {
            double pixelWidthHeightRatio = mVideoSarNum / (mVideoSarDen / 1.0);
            realWidth = (int) (pixelWidthHeightRatio * mVideoWidth);
        }

        int width = View.getDefaultSize(realWidth, widthMeasureSpec);
        int height = View.getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mAspectRatio == AspectRatio.RATIO_MATCH_FULL) {
            width = widthMeasureSpec;
            height = heightMeasureSpec;
        } else if (realWidth > 0 && mVideoHeight > 0) {
            int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == View.MeasureSpec.AT_MOST && heightSpecMode == View.MeasureSpec.AT_MOST) {
                float specAspectRatio = (float) widthSpecSize / (float) heightSpecSize;
                float displayAspectRatio;
                switch (mAspectRatio) {
                    case AspectRatio.RATIO_16_9:
                        displayAspectRatio = 16.0f / 9.0f;
                        if (mRotationDegree == 90 || mRotationDegree == 270)
                            displayAspectRatio = 1.0f / displayAspectRatio;
                        break;
                    case AspectRatio.RATIO_18_9:
                        displayAspectRatio = 18.0f / 9.0f;
                        if (mRotationDegree == 90 || mRotationDegree == 270)
                            displayAspectRatio = 1.0f / displayAspectRatio;
                        break;
                    case AspectRatio.RATIO_4_3:
                        displayAspectRatio = 4.0f / 3.0f;
                        if (mRotationDegree == 90 || mRotationDegree == 270)
                            displayAspectRatio = 1.0f / displayAspectRatio;
                        break;
                    case AspectRatio.RATIO_CUSTOM:
                        displayAspectRatio = mCustomAspectRatio;
                        if (mRotationDegree == 90 || mRotationDegree == 270)
                            displayAspectRatio = 1.0f / displayAspectRatio;
                        break;
                    case AspectRatio.RATIO_DEFAULT:
                    case AspectRatio.RATIO_FULL:
                        //case AspectRatio.AR_ASPECT_WRAP_CONTENT:
                    default:
                        displayAspectRatio = (float) realWidth / (float) mVideoHeight;
                        if (mVideoSarNum > 0 && mVideoSarDen > 0)
                            displayAspectRatio = displayAspectRatio * mVideoSarNum / mVideoSarDen;
                        break;
                }
                boolean shouldBeWider = displayAspectRatio > specAspectRatio;

                switch (mAspectRatio) {
                    case AspectRatio.RATIO_DEFAULT:
                    case AspectRatio.RATIO_16_9:
                    case AspectRatio.RATIO_4_3:
                    case AspectRatio.RATIO_18_9:
                    case AspectRatio.RATIO_CUSTOM:
                        if (shouldBeWider) {
                            // too wide, fix width
                            width = widthSpecSize;
                            height = (int) (width / displayAspectRatio);
                        } else {
                            // too high, fix height
                            height = heightSpecSize;
                            width = (int) (height * displayAspectRatio);
                        }
                        break;
                    case AspectRatio.RATIO_FULL:
                        if (shouldBeWider) {
                            // not high enough, fix height
                            height = heightSpecSize;
                            width = (int) (height * displayAspectRatio);
                        } else {
                            // not wide enough, fix width
                            width = widthSpecSize;
                            height = (int) (width / displayAspectRatio);
                        }
                        break;
                    //case AspectRatio.AR_ASPECT_WRAP_CONTENT:
                    default:
                        if (shouldBeWider) {
                            // too wide, fix width
                            width = Math.min(realWidth, widthSpecSize);
                            height = (int) (width / displayAspectRatio);
                        } else {
                            // too high, fix height
                            height = Math.min(mVideoHeight, heightSpecSize);
                            width = (int) (height * displayAspectRatio);
                        }
                        break;
                }
            } else if (widthSpecMode == View.MeasureSpec.EXACTLY && heightSpecMode == View.MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (realWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * realWidth / mVideoHeight;
                } else if (realWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / realWidth;
                }
            } else if (widthSpecMode == View.MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / realWidth;
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == View.MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * realWidth / mVideoHeight;
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = realWidth;
                height = mVideoHeight;
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * realWidth / mVideoHeight;
                }
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / realWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        this.mMeasuredWidth = width;
        this.mMeasuredHeight = height;
    }

    public void prepareMeasure(int widthMeasureSpec, int heightMeasureSpec, int degree) {
        try {
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                this.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
            }
            this.setRotationDegrees(degree);
            this.doMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }
}