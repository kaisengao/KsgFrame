package com.kaisengao.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.kaisengao.base.transformer.GlideRoundTransform;

/**
 * @ClassName: GlideUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:02
 * @Description: Glide 简单工具类
 */
public final class GlideUtil {

    /**
     * Load 加载图片
     *
     * @param context   context
     * @param url       url
     * @param imageView imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * Load 加载图片
     *
     * @param context         context
     * @param url             url
     * @param imageViewTarget requestListener
     */
    public static void loadImage(Context context, String url, ImageViewTarget<Drawable> imageViewTarget) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewTarget);
    }

    /**
     * Load 加载图片Bitmap
     *
     * @param context   context
     * @param url       url
     * @param imageView imageView
     */
    public static void loadImageBitmap(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * Load 加载图片Override
     *
     * @param context   context
     * @param url       url
     * @param width     width
     * @param height    height
     * @param imageView imageView
     */
    public static void loadImageOverride(Context context, String url, int width, int height, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * Load 加载图片Crop
     *
     * @param context   context
     * @param url       url
     * @param imageView imageView
     */
    public static void loadImageCrop(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * Load 加载图片Circle
     *
     * @param context   context
     * @param url       url
     * @param imageView imageView
     */
    public static void loadImageCircle(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
                .transform(new CircleCrop())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * Load 加载图片Round
     *
     * @param context   context
     * @param url       url
     * @param imageView imageView
     * @param radius    radius
     */
    public static void loadImageRound(Context context, Object url, ImageView imageView, int radius) {
        Glide.with(context)
                .load(url)
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
