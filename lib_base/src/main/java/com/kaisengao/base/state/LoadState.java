package com.kaisengao.base.state;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @ClassName: LoadState
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/18 15:23
 * @Description:
 */
@IntDef({LoadState.INITIAL, LoadState.LOADING, LoadState.SUCCESS, LoadState.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadState {

    int INITIAL = -1;

    int LOADING = 101;

    int SUCCESS = 102;

    int ERROR = 103;

}
