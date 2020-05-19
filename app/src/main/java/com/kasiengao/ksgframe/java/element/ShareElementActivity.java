package com.kasiengao.ksgframe.java.element;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.base.util.GlideUtil;
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

        AppCompatImageView picture = findViewById(R.id.share_element_picture);
        AppCompatTextView content = findViewById(R.id.share_element_content);

        // LoadPicture
        int itemWidth = (int) DensityUtil.getWidthInPx(this);
        float scale = (itemWidth + 0f) / mGridBean.mWidth;
        int itemHeight = Math.round(mGridBean.mHeight * scale);
        GlideUtil.loadImage(this, mGridBean.mPicture, itemWidth, itemHeight, picture);

        // LoadContent
        content.setText(mGridBean.mDetailContent);

        ViewCompat.setTransitionName(picture, getString(R.string.share_element_picture) + mPosition);
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
