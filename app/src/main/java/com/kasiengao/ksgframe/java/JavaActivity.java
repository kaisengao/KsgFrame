package com.kasiengao.ksgframe.java;

import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;

/**
 * @ClassName: JavaActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 18:43
 * @Description: Java 版本
 */
public class JavaActivity extends BaseToolbarActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_java;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.java_title);
    }
}
