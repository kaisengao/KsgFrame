package com.kaisengao.base.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/**
 * @ClassName: BlurTransformation
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 16:39
 * @Description: 毛玻璃
 */
public class BlurTransform extends BitmapTransformation {

    private final int radius;

    private final RenderScript mRenderScript;

    public BlurTransform(Context context, int radius) {
        super();
        this.radius = radius;
        this.mRenderScript = RenderScript.create(context);
    }

    @Override
    protected Bitmap transform(@NotNull BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);
        // 分配用于渲染脚本的内存
        Allocation input = Allocation.createFromBitmap(mRenderScript, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(mRenderScript, input.getType());
        // 加载我们想要使用的特定脚本的实例。
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
        script.setInput(input);
        // 设置模糊半径
        script.setRadius(radius);
        // 启动Script,
        script.forEach(output);
        // 将输出复制到模糊的位图
        output.copyTo(blurredBitmap);

        return blurredBitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update("blur transformation".getBytes());
    }
}