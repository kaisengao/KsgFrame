package com.kasiengao.base.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.kasiengao.base.transformer.GlideRoundTransform;

/**
 * @ClassName: GlideUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:02
 * @Description: Glide 简单工具类
 */
public final class GlideUtil {

    /**
     * 普通图片
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .priority(Priority.HIGH)
//                .error(failureIcon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 普通图片
     */
    public static void loadImage(Context context, String url, int width, int height, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
//                .error(failureIcon)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 普通图片 Crop
     */
    public static void loadImageCrop(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 圆形图片
     */
    public static void loadImageCircle(Context context, String url, ImageView imageView, int failureIcon) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
                .transform(new CircleCrop())
                .error(failureIcon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 图圆角片
     *
     * @param radius 圆角
     */
    public static void loadImageRound(Context context, Object url, ImageView imageView, int radius) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(radius))
//                .error(failureIcon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
