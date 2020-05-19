package com.kasiengao.base.transformer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * @ClassName: GlideRoundTransform
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:02
 * @Description: 圆角Glide
 */
public class GlideRoundTransform extends BitmapTransformation {
    private float mRadius;

    public GlideRoundTransform() {
        this(4);
    }

    public GlideRoundTransform(int dp) {
        super();
        this.mRadius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(@androidx.annotation.NonNull BitmapPool pool, @androidx.annotation.NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, bitmap);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);
        return result;
    }

    public String getId() {
        return getClass().getName() + Math.round(mRadius);
    }

    @Override
    public void updateDiskCacheKey(@androidx.annotation.NonNull MessageDigest messageDigest) {
    }
}
