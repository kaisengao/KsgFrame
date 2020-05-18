package com.kasiengao.ksgframe.java;

import android.content.Intent;
import android.view.Window;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.element.ShareElementActivity;
import com.kasiengao.ksgframe.java.mvp.MvpActivity;
import com.kasiengao.ksgframe.java.retrofit.RxRetrofitActivity;
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
    protected void initWindow() {
        super.initWindow();
        // 打开FEATURE_CONTENT_TRANSITIONS开关(可选)，这个开关默认是打开的
        super.requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
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
        // Rx+Retrofit
        this.findViewById(R.id.java_rx_retrofit).setOnClickListener(v -> {
            // Rx+Retrofit
            this.startActivity(new Intent(this, RxRetrofitActivity.class));
        });
        // ShareElement
        AppCompatImageButton shareElement = this.findViewById(R.id.java_share_element);
        shareElement.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShareElementActivity.class);
            ActivityOptionsCompat activityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, shareElement, getString(R.string.share_element_name));
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
        });

    }
}
