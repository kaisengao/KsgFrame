package com.kasiengao.ksgframe.ui.trainee.element;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.kaisengao.base.util.StatusBarUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.ui.trainee.element.preview.PreviewBean;
import com.kasiengao.ksgframe.ui.trainee.element.preview.PreviewPager;
import com.kasiengao.ksgframe.ui.trainee.staggered.StaggeredGridBean;
import com.kasiengao.mvp.java.BaseToolbarActivity;

/**
 * @ClassName: ShareElement
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/18 8:20 PM
 * @Description:
 */
public class ShareElementActivity extends BaseToolbarActivity {

    public static final String DATA = "DATA";

    public static final String POSITION = "POSITION";

    private int mPosition;

    private PlayerContainerView mPlayerContainer;

    private PreviewPager<PreviewBean> mPreviewPager;

    private StaggeredGridBean mGridBean;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_share_element;
    }

    @Override
    protected boolean isDisplayToolbar() {
        return false;
    }

    @Override
    protected void initWindow() {
        super.initWindow();
        // 解决 页面跳转的时候SimpleDraweeView不显示图片
        this.getWindow().setSharedElementEnterTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
                        ScalingUtils.ScaleType.CENTER_CROP)); // 进入
        this.getWindow().setSharedElementReturnTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER,
                        ScalingUtils.ScaleType.CENTER_CROP)); // 返回
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);

        this.mPosition = bundle.getInt(POSITION, -1);
        this.mGridBean = (StaggeredGridBean) bundle.getSerializable(DATA);
    }

    @Override
    protected void initBefore() {
        StatusBarUtil.StatusBarLightMode(this);
        StatusBarUtil.transparencyBar(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.share_element_title);
        // Views
        this.mPlayerContainer = findViewById(R.id.share_player_container);
        this.mPreviewPager = findViewById(R.id.share_element_pager);
        // Init Preview
        this.initPreview();
        // LoadContent
        AppCompatTextView content = findViewById(R.id.share_element_content);
        content.setText(mGridBean.mDetailContent);
    }

    /**
     * Init Preview
     */
    private void initPreview() {
        this.mPreviewPager.setPlayerContainer(mPlayerContainer);
        // Data
        this.mPreviewPager.setMediaList(mGridBean.mPreviewBeans);
        // ShareElement
        ViewCompat.setTransitionName(mPreviewPager, getString(R.string.share_element_picture) + mPosition);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onBackPressed() {
        // 1、验证横竖屏
        if (mPreviewPager.isLandscape()) {
            this.mPreviewPager.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        // 2、验证全屏
        if (mPreviewPager.isFullscreen()) {
            this.mPreviewPager.setFullscreen(false);
            this.mPreviewPager.onFullscreen(false);
            return;
        }
        // 剩下的基操给系统
        super.onBackPressed();
    }
}
