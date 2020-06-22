package com.kasiengao.ksgframe.java.retrofit;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.retrofit.RxCompose;
import com.kaisengao.retrofit.observer.BaseDialogObserver;
import com.kaisengao.retrofit.observer.mvp.BaseLoadSirObserver;
import com.kaisengao.retrofit.observer.BaseRxObserver;
import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @ClassName: RxRetrofitActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:46
 * @Description: Rx+Retrofit
 */
public class RxRetrofitActivity extends BaseToolbarActivity {

    private AppCompatTextView mMessage;

    private RxRetrofitModel mModel;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_rx_retrofit;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.rx_retrofit_title);

        this.mMessage = findViewById(R.id.message);
    }

    @Override
    protected void initData() {
        super.initData();
        // Model
        this.mModel = new RxRetrofitModel();
        // 默认请求
        this.requestLoadSir();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rx_retrofit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_normal:
                this.requestNormal();
                break;
            case R.id.menu_dialog:
                this.requestDialog();
                break;
            case R.id.menu_loadsir:
                this.requestLoadSir();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Normal 请求
     */
    private void requestNormal() {

        mMessage.setText("");

        this.requestNewsTop()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new BaseRxObserver<NewsTopBean>(this) {
                    @Override
                    protected void onResult(NewsTopBean topBean) {

                        if (topBean != null) {
                            mMessage.setText("Success");
                        }
                    }
                });
    }

    /**
     * Dialog 请求
     */
    private void requestDialog() {

        mMessage.setText("");

        this.requestNewsTop()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new BaseDialogObserver<NewsTopBean>(this) {
                    @Override
                    protected void onResult(NewsTopBean topBean) {

                        if (topBean != null) {
                            mMessage.setText("Success");
                        }
                    }
                });
    }

    /**
     * LoadSir 请求
     */
    private void requestLoadSir() {

        mMessage.setText("");

        this.requestNewsTop()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new BaseLoadSirObserver<NewsTopBean>(this, getInflate()) {
                    @Override
                    protected void onResult(NewsTopBean topBean) {

                        if (topBean != null) {
                            mMessage.setText("Success");
                        }
                    }

                    @Override
                    protected void onReload(Object target) {
                        // Reload
                        requestLoadSir();
                    }
                });
    }

    /**
     * 聚合数据 新闻
     */
    private Observable<NewsTopBean> requestNewsTop() {
        return Observable
                // 延时3秒
                .timer(3, TimeUnit.SECONDS)
                // 聚合数据
                .flatMap((Function<Long, ObservableSource<NewsTopBean>>) aLong -> mModel.requestNewsTop());
    }
}
