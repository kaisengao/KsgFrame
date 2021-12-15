package com.kasiengao.ksgframe.ui.trainee.loadpage;

import android.app.Activity;
import android.view.View;

import com.kaisengao.base.loadpage.KsgLoadFrame;
import com.kaisengao.base.loadpage.load.EmptyViewLoad;
import com.kaisengao.base.loadpage.load.ErrorViewLoad;
import com.kaisengao.base.loadpage.load.LoadingViewLoad;
import com.kaisengao.base.loadpage.load.base.ILoad;
import com.kaisengao.base.loadpage.widget.LoadContainer;
import com.kaisengao.base.util.ToastUtil;
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
        LoadContainer container = KsgLoadFrame.bindLoadContainer(this, this::onLoadClick);
        // LoadData
        this.requestData()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable disposable) {
                        // Loading
                        container.showLoadView(LoadingViewLoad.class);
                    }

                    @Override
                    public void onNext(@NotNull Long time) {
                        LoadingViewLoad curLoadView = (LoadingViewLoad) container.getCurLoadView();
                        curLoadView.setLoadingText("倒计时：" + (time + 1) + "s");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        // Complete
                        container.showOriginalView();

                        loadView0();
                        loadView1();
                        loadView2();
                        loadView3();
                    }
                });
    }

    private void loadView0() {
        LoadContainer container = KsgLoadFrame.bindLoadContainer(mBinding.load0, this::onLoadClick);
        this.mBinding.load0.findViewById(R.id.load0).setOnClickListener(v -> loadData0(container));
        this.loadData0(container);
    }

    private void loadData0(LoadContainer container) {
        // LoadData
        this.requestData()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable disposable) {
                        // Loading
                        container.showLoadView(LoadingViewLoad.class);
                    }

                    @Override
                    public void onNext(@NotNull Long time) {
                        LoadingViewLoad curLoadView = (LoadingViewLoad) container.getCurLoadView();
                        curLoadView.setLoadingText("倒计时：" + (time + 1) + "s");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        // Complete
                        container.showOriginalView();
                    }
                });
    }

    private void loadView1() {
        LoadContainer container = KsgLoadFrame.bindLoadContainer(mBinding.load1);
        this.loadData1(container);
    }

    private void loadData1(LoadContainer container) {
        // LoadData
        this.requestData()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable disposable) {
                        // Loading
                        container.showLoadView(LoadingViewLoad.class);
                    }

                    @Override
                    public void onNext(@NotNull Long time) {
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void loadView2() {
        LoadContainer container = KsgLoadFrame.bindLoadContainer(mBinding.load2, this::onLoadClick);
        this.loadData2(container);
    }

    private void loadData2(LoadContainer container) {
        // LoadData
        this.requestData()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable disposable) {
                        // Loading
                        container.showLoadView(LoadingViewLoad.class);
                    }

                    @Override
                    public void onNext(@NotNull Long time) {
                        LoadingViewLoad curLoadView = (LoadingViewLoad) container.getCurLoadView();
                        curLoadView.setLoadingText("倒计时：" + (time + 1) + "s");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        // Empty
                        container.showLoadView(EmptyViewLoad.class);
                    }
                });
    }

    private void loadView3() {
        LoadContainer container = KsgLoadFrame.bindLoadContainer(mBinding.load3, this::onLoadClick);
        this.loadData3(container);
    }

    private void loadData3(LoadContainer container) {
        // LoadData
        this.requestData()
                .as(RxCompose.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable disposable) {
                        // Loading
                        container.showLoadView(LoadingViewLoad.class);
                    }

                    @Override
                    public void onNext(@NotNull Long time) {
                        LoadingViewLoad curLoadView = (LoadingViewLoad) container.getCurLoadView();
                        curLoadView.setLoadingText("倒计时：" + (time + 1) + "s");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        // Error
                        container.showLoadView(ErrorViewLoad.class);
                        ErrorViewLoad curLoadView = (ErrorViewLoad) container.getCurLoadView();
                        curLoadView.setErrorText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        onError(new IllegalArgumentException("手动异常，见笑见笑"));
                    }
                });
    }

    /**
     * LoadView 点击事件
     */
    public void onLoadClick(Object target, ILoad load) {
        LoadContainer container = (LoadContainer) ((ILoad) load).getRootView().getParent();

        if (target instanceof Activity) {
            ToastUtil.showShort("Activity!");
        } else if (target instanceof View) {
            if (target == mBinding.load0) {
                ToastUtil.showShort("Normal Click!");
            } else if (target == mBinding.load2) {
                if (load instanceof EmptyViewLoad) {
                    ToastUtil.showShort("Empty Click!");
                }
            } else if (target == mBinding.load3) {
                if (load instanceof ErrorViewLoad) {
                    loadData3(container);
                }
            }
        }
    }

    /**
     * 模拟加载数据
     */
    private Observable<Long> requestData() {
        return Observable
                .interval(0, 1000, TimeUnit.MILLISECONDS)
                .take(3)
                .compose(RxCompose.applySchedulers());
    }

}