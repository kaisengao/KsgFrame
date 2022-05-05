package com.kaisengao.base.transform;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/**
 * @ClassName: GlideRoundTransform
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:02
 * @Description: 圆角Glide
 */
public class RoundTransform extends BitmapTransformation {

    private final float mRadius;

    public RoundTransform() {
        this(4);
    }

    public RoundTransform(int dp) {
        super();
        this.mRadius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(@NotNull BitmapPool pool, @NotNull Bitmap toTransform, int outWidth, int outHeight) {
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

    @Override
    public void updateDiskCacheKey(@androidx.annotation.NonNull MessageDigest messageDigest) {
    }

    public String getId() {
        return getClass().getName() + Math.round(mRadius);
    }

}
