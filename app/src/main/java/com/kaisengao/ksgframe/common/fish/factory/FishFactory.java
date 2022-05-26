package com.kaisengao.ksgframe.common.fish.factory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.SparseArray;

import com.kaisengao.ksgframe.common.fish.anim.base.BaseAuxiliaryFishAnim;
import com.kaisengao.ksgframe.common.fish.helper.FishHelper;
import com.kaisengao.ksgframe.common.util.BitmapUtils;

import java.util.List;

/**
 * @ClassName: FishFactory
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/3 15:37
 * @Description:
 */
public class FishFactory {

    private static FishFactory instance;

    private SparseArray<Bitmap> mFishAuxiliary01Caches;

    private SparseArray<Bitmap> mFishAuxiliary02Caches;

    private SparseArray<Bitmap> mFishAuxiliary03Caches;

    public static FishFactory getInstance() {
        if (instance == null) {
            synchronized (FishFactory.class) {
                if (instance == null) {
                    instance = new FishFactory();
                }
            }
        }
        return instance;
    }

    private FishFactory() {
    }

    /**
     * {@link BaseAuxiliaryFishAnim} （小丑鱼）bitmap缓存列表
     *
     * @param context context
     */
    public SparseArray<Bitmap> getFishAuxiliary01Caches(Context context) {
        if (mFishAuxiliary01Caches == null) {
            this.mFishAuxiliary01Caches = decodeBitmap(context, FishHelper.FISH_LIST_AUXILIARY_01, 1.3f);
        }
        return mFishAuxiliary01Caches;
    }

    /**
     * {@link BaseAuxiliaryFishAnim} （小黄鱼）bitmap缓存列表
     *
     * @param context context
     */
    public SparseArray<Bitmap> getFishAuxiliary02Caches(Context context) {
        if (mFishAuxiliary02Caches == null) {
            this.mFishAuxiliary02Caches = decodeBitmap(context, FishHelper.FISH_LIST_AUXILIARY_02, 1f);
        }
        return mFishAuxiliary02Caches;
    }

    /**
     * {@link BaseAuxiliaryFishAnim} (大蓝鱼)bitmap缓存列表
     *
     * @param context context
     */
    public SparseArray<Bitmap> getFishAuxiliary03Caches(Context context) {
        if (mFishAuxiliary03Caches == null) {
            this.mFishAuxiliary03Caches = decodeBitmap(context, FishHelper.FISH_LIST_AUXILIARY_03, 0.45f);
        }
        return mFishAuxiliary03Caches;
    }

    /**
     * 解码图片并缓存
     *
     * @param ids   资源列表
     * @param scale 缩放比例（0 - 1）
     */
    protected SparseArray<Bitmap> decodeBitmap(Context context, List<Integer> ids, float scale) {
        SparseArray<Bitmap> bitmapCaches = new SparseArray<>();
        // 缩放
        Matrix matrix = new Matrix();
        matrix.preScale(scale, scale);
        // 遍历
        int idsSize = ids.size();
        for (int i = 0; i < idsSize; i++) {
            bitmapCaches.put(i, BitmapUtils.decodeBitmap(context.getResources(), ids.get(i), scale));
        }
        return bitmapCaches;
    }
}