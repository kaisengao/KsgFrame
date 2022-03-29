package com.kasiengao.ksgframe.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kasiengao.ksgframe.R;

/**
 * @ClassName: CSwipeRefreshLayout
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/26 17:48
 * @Description:
 */
public class CSwipeRefreshLayout extends SwipeRefreshLayout {

    public CSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public CSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        // 设置 刷新圆圈的颜色
        this.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        // 设置 刷新圆圈的背景色
        this.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        // 设置 刷新圆圈的下拉距离
        this.setDistanceToTriggerSync(600);
    }
}