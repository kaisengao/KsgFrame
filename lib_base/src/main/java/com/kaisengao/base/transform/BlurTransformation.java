package com.kaisengao.base.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @ClassName: BlurTransformation
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/5 20:01
 * @Description: 毛玻璃
 */
public class BlurTransformation extends BitmapTransformation {

    private final RenderScript mRenderScript;

    private final int mRadius;

    public BlurTransformation(Context context, int radius) {
        super();
        this.mRadius = radius;
        this.mRenderScript = RenderScript.create(context);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);
        // 分配用于渲染脚本的内存
        Allocation input = Allocation.createFromBitmap(mRenderScript, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(mRenderScript, input.getType());
        // 加载我们想要使用的特定脚本的实例。
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
        script.setInput(input);
        // 设置模糊半径
        script.setRadius(mRadius);
        // 启动ScriptIntrinsicBlur
        script.forEach(output);
        // 将输出复制到模糊的位图
        output.copyTo(blurredBitmap);
        return blurredBitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update("blur transformation".getBytes());
    }
}
