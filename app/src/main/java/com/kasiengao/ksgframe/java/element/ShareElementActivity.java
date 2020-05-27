package com.kasiengao.ksgframe.java.element;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.staggered.StaggeredGridBean;
import com.kasiengao.ksgframe.java.widget.PlayerContainerView;
import com.kasiengao.mvp.java.BaseToolbarActivity;

import butterknife.BindView;

/**
 * @ClassName: ShareElement
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/18 8:20 PM
 * @Description:
 */
public class ShareElementActivity extends BaseToolbarActivity {

    public static final String DATA = "DATA";

    public static final String POSITION = "POSITION";

    @BindView(R.id.share_player_container)
    PlayerContainerView mPlayerContainer;
    @BindView(R.id.share_element_pager)
    PreviewPager<PreviewBean> mPreviewPager;

    private int mPosition;

    private StaggeredGridBean mGridBean;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_share_element;
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
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.share_element_title);
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
        // Data
        this.mPreviewPager.setMediaList(mGridBean.mPreviewBeans);
        // ShareElement
        ViewCompat.setTransitionName(mPreviewPager, getString(R.string.share_element_picture) + mPosition);
    }

    @Override
    protected void onClickBack() {
        this.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        this.mPreviewPager.onConfigurationChanged(mPlayerContainer, newConfig);

        boolean landScape = this.mPreviewPager.isLandScape();

        if(landScape){
            getSupportActionBar().hide();
        }else {
            getSupportActionBar().show();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.mPreviewPager.isLandScape()) {
            super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }
}
