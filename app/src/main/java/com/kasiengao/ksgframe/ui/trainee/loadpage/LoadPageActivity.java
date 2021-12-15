package com.kasiengao.ksgframe.ui.trainee.loadpage;

import android.view.View;

import com.kaisengao.base.loadpage.KsgLoadFrame;
import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;
import com.kaisengao.base.loadpage.load.BaseLoad;
import com.kaisengao.base.loadpage.load.ErrorViewLoad;
import com.kaisengao.base.loadpage.load.LoadingViewLoad;
import com.kaisengao.base.loadpage.widget.LoadContainer;
import com.kaisengao.mvvm.base.activity.BaseActivity;
import com.kaisengao.retrofit.RxCompose;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.ActivityLoadpageBinding;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @ClassName: LoadPageActivity
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 14:59
 * @Description: LoadActivity
 */
public class LoadPageActivity extends BaseActivity<ActivityLoadpageBinding> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_loadpage;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // RootView
        this.loadRootView();
    }

    private void loadRootView() {
        LoadContainer container = KsgLoadFrame.bindLoadContainer(mBinding.loadRoot, mLoadViewClickListener);
        this.loadData(container);
    }

    private void loadView1() {
        LoadContainer container = KsgLoadFrame.bindLoadContainer(mBinding.load1, (target, load) -> {
            BaseLoad baseLoad = (BaseLoad) load;
            ((LoadContainer) baseLoad.getRootView().getParent()).showOriginalView();
        });
        this.loadData(container);
    }

    /**
     * LoadView 点击事件
     */
    private final OnLoadViewClickListener mLoadViewClickListener = (target, load) -> {
        // 这里做重试操作
        // -----------------------------------------------
        // 我这里测试直接显示原视图了
        BaseLoad baseLoad = (BaseLoad) load;
        if (load instanceof ErrorViewLoad) {
            ((LoadContainer) baseLoad.getRootView().getParent()).showOriginalView();

            if (target instanceof View) {
                View targetView = (View) target;
                if (targetView == mBinding.loadRoot) {
                    this.loadView1();
                }
            }

        }
    };

    /**
     * 模拟加载数据
     */
    private void loadData(LoadContainer container) {
        Observable
                .interval(0, 1000, TimeUnit.MILLISECONDS)
                .take(3)
                .compose(RxCompose.applySchedulers())
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable disposable) {
                        // Loading
                        container.showLoad(LoadingViewLoad.class);
                    }

                    @Override
                    public void onNext(@NotNull Long time) {
                        LoadingViewLoad curLoadView = (LoadingViewLoad) container.getCurLoadView();
                        curLoadView.setLoadingText("倒计时：" + (time + 1) + "s");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        // Error
                        container.showLoad(ErrorViewLoad.class);
                        ErrorViewLoad curLoadView = (ErrorViewLoad) container.getCurLoadView();
                        curLoadView.setErrorText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // Complete
//                        container.showOriginalView();
                        onError(new IllegalArgumentException("手动异常，见笑见笑"));
                    }
                });
    }
}