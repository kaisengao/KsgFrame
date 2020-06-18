package com.kasiengao.base.loadsir.callback;

import android.content.Context;
import android.view.View;

import com.kasiengao.base.R;

/**
 * @ClassName: ErrorCallback
 * @Author: KaiSenGao
 * @CreateDate: 2019-07-04 10:51
 * @Description: 错误页面
 */
public class ErrorCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_error;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        view.findViewById(R.id.error_retry).setOnClickListener(v -> {
            if (getOnReloadListener() != null) {
                getOnReloadListener().onReload(view);
            }
        });
        return true;
    }
}
