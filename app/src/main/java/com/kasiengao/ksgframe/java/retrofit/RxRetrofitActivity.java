package com.kasiengao.ksgframe.java.retrofit;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.retrofit.RxCompose;
import com.kaisengao.retrofit.observer.BaseDialogObserver;
import com.kaisengao.retrofit.observer.BaseLoadSirObserver;
import com.kaisengao.retrofit.observer.BaseRxObserver;
import com.kasiengao.base.util.KLog;
import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;

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
        this.mModel = new RxRetrofitModel();
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

        this.mModel
                .requestNewsTop()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new BaseRxObserver<NewsTopBean>(this) {
                    @Override
                    protected void onResult(NewsTopBean topBean) {

                        if (topBean != null) {
                            mMessage.setText(topBean.toString());
                        }
                    }
                });
    }

    /**
     * Dialog 请求
     */
    private void requestDialog() {

        this.mModel
                .requestNewsTop()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new BaseDialogObserver<NewsTopBean>(this) {
                    @Override
                    protected void onResult(NewsTopBean topBean) {

                        if (topBean != null) {
                            mMessage.setText(topBean.toString());
                        }
                    }
                });
    }

    /**
     * LoadSir 请求
     */
    private void requestLoadSir() {

        this.mModel
                .requestNewsTop()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new BaseLoadSirObserver<NewsTopBean>(this, getInflate()) {
                    @Override
                    protected void onResult(NewsTopBean topBean) {
                        KLog.d("3 +" + topBean);

                        if (topBean != null) {
                            mMessage.setText(topBean.toString());
                        }
                    }

                    @Override
                    protected void onReload(Object target) {

                    }
                });
    }
}
