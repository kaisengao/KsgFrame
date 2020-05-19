package com.kasiengao.ksgframe.java.element;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.kasiengao.base.util.DensityUtil;
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

    private int[] mImgheights;

    private ViewPager mViewPager;

    private StaggeredGridBean mGridBean;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_share_element;
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
        // Init ViewPager
        this.initViewPager();
        // LoadContent
        AppCompatTextView content = findViewById(R.id.share_element_content);
        content.setText(mGridBean.mDetailContent);
    }

    /**
     * Init ViewPager
     */
    private void initViewPager() {
        // findViewById
        this.mViewPager = findViewById(R.id.share_element_pager);
        // 默认显示第一张图
        PreviewBean previewBean = this.mGridBean.mPreviewBeans.get(0);
        // 获取item宽度，计算图片等比例缩放后的高度，为ViewPager设置参数
        int itemWidth = (int) DensityUtil.getWidthInPx(this);
        float scale = (itemWidth + 0f) / previewBean.mWidth;
        int itemHeight = (int) (previewBean.mHeight * scale);
        // 重新赋值
        ViewGroup.LayoutParams layoutParams = this.mViewPager.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemHeight;
        this.mViewPager.setLayoutParams(layoutParams);
        // ShareElement
        ViewCompat.setTransitionName(mViewPager, getString(R.string.share_element_picture) + mPosition);
        // ImageHeight
        if (this.mImgheights == null || mImgheights.length != this.mGridBean.mPreviewBeans.size()) {
            this.mImgheights = null;
            this.mImgheights = new int[this.mGridBean.mPreviewBeans.size()];
        }
        // Adapter
        PreviewPagerAdapter pagerAdapter = new PreviewPagerAdapter(this, mImgheights, mGridBean.mPreviewBeans);
        this.mViewPager.setAdapter(pagerAdapter);
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == mImgheights.length - 1) {
                    return;
                }
                PreviewBean bean = mGridBean.mPreviewBeans.get(position);

                //计算ViewPager现在应该的高度,heights[]表示页面高度的数组。
                int height = (int) ((mImgheights[position] == 0 ? itemHeight : mImgheights[position]) * (1 - positionOffset) +
                        (mImgheights[position + 1] == 0 ? itemHeight : mImgheights[position + 1]) * positionOffset);
                ViewGroup.LayoutParams params = mViewPager.getLayoutParams();//为ViewPager设置高度
                params.height = height;
                mViewPager.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
