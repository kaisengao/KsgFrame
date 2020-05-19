package com.kasiengao.ksgframe.java.element;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.base.util.GlideUtil;

import java.util.List;

/**
 * @ClassName: PreviewPagerAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 16:30
 * @Description: 预览资源 ViewPager
 */
public class PreviewPagerAdapter extends PagerAdapter {

    private final Context mContext;

    private final int[] mImgheights;

    private final List<PreviewBean> mPreviewBeans;

    public PreviewPagerAdapter(Context context, int[] imgheights, List<PreviewBean> previewBeans) {
        this.mContext = context;
        this.mImgheights = imgheights;
        this.mPreviewBeans = previewBeans;
    }

    @Override
    public int getCount() {
        return this.mPreviewBeans == null ? 0 : mPreviewBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PreviewBean previewBean = this.mPreviewBeans.get(position);
        // ImageView
        final ImageView imageView = new ImageView(mContext);

        float scale = (float) previewBean.mHeight / previewBean.mWidth;
        int height = (int) (scale * DensityUtil.getWidthInPx(container.getContext()));
        mImgheights[position] = height;

        GlideUtil.loadImage(mContext, previewBean.mMediaUrl, imageView);
        container.addView(imageView);
        return imageView;
    }
}
