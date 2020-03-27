package com.kasiengao.ksgframe.java;

import android.content.Intent;

import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.mvp.MvpActivity;
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

        // Mvp 模式
        this.findViewById(R.id.java_mvp).setOnClickListener(v -> {
            // Mvp 模式
            this.startActivity(new Intent(this, MvpActivity.class));
        });

    }
}
