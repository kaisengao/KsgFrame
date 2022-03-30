package com.kaisengao.mvvm.binding.adapter.glide;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.kaisengao.base.util.GlideUtil;

/**
 * @ClassName: GlideAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/9/9 15:26
 * @Description:
 */
public class GlideAdapter {

    @BindingAdapter("loadImage")
    public static void loadImage(AppCompatImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            GlideUtil.loadImage(imageView.getContext(), url, imageView);
        }
    }
}
