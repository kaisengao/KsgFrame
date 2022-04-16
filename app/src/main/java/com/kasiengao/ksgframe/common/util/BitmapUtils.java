package com.kasiengao.ksgframe.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * @ClassName: BitmapUtils
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/3 15:40
 * @Description:
 */
public class BitmapUtils {

    /**
     * 获取 Vector资源图标
     *
     * @param resId resId
     * @return Bitmap
     */
    public static Bitmap getVectorBitmap(Context context, int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }

    /**
     * 解码图片
     *
     * @param resId 资源
     * @param scale 缩放比例（0 - 1）
     * @return 压缩过Bitmap
     */
    public static Bitmap decodeBitmap(Resources res, int resId, float scale) {
        // 读取图片宽高
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        int reqWidth = (int) (options.outWidth * scale);
        int reqHeight = (int) (options.outHeight * scale);
        // 计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        // 载入一个稍大的缩略图
        Bitmap src = BitmapFactory.decodeResource(res, resId, options);
        // 创建新的缩放Bitmap
        return createScaleBitmap(src, reqWidth, reqHeight);
    }

    /**
     * 计算inSampleSize值
     *
     * @param options   参数
     * @param reqWidth  目标的宽度
     * @param reqHeight 目标的高度
     * @return 计算图片的压缩比率
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 创建新的缩放Bitmap
     *
     * @param src       原Bitmap
     * @param dstWidth  缩放的宽
     * @param dstHeight 缩放的高
     * @return 得到符合标准的bitmap
     */
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        // 如果没有缩放，那么不回收
        if (src != dst) {
            // 释放Bitmap的native像素数组
            src.recycle();
        }
        return dst;
    }
}