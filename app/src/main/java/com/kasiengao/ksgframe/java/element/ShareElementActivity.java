package com.kasiengao.ksgframe.java.element;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.staggered.StaggeredGridBean;
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

    private StaggeredGridBean mGridBean;

    private PreviewPager<PreviewBean> mPreviewPager;

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
        // findViewById
        this.mPreviewPager = findViewById(R.id.share_element_pager);
        // Data
        this.mPreviewPager.setMediaList(mGridBean.mPreviewBeans);
        // ShareElement
        ViewCompat.setTransitionName(mPreviewPager, getString(R.string.share_element_picture) + mPosition);
    }

    @Override
    protected void onClickBack() {
        this.supportFinishAfterTransition();
    }
}
