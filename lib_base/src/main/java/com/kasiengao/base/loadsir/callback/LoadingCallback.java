package com.kasiengao.base.loadsir.callback;

import android.content.Context;
import android.view.View;

import com.kasiengao.base.R;

/**
 * @ClassName: LoadingCallback
 * @Author: KaiSenGao
 * @CreateDate: 2019-07-04 10:52
 * @Description: Loading
 */
public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }


    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return true;
    }
}
