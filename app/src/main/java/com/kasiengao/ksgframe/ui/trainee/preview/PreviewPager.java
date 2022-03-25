package com.kasiengao.ksgframe.ui.trainee.preview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ClassName: PreviewPager
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/20 10:43
 * @Description: 自适应 预览 Banner
 */
public class PreviewPager<T extends IPreviewParams> extends FrameLayout  {


    public PreviewPager(@NonNull Context context) {
        this(context, null);
    }

    public PreviewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
