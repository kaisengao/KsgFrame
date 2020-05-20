package com.kasiengao.ksgframe.java.element;

import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import com.kasiengao.base.util.KLog;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.staggered.StaggeredGridBean;
import com.kasiengao.mvp.java.BaseToolbarActivity;

import java.util.List;
import java.util.Map;

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
        // 监听共享元素结束后 刷新一下Viewpager
        this.setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                KLog.d("zzz", "onSharedElementStart");

            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                KLog.d("zzz", "onSharedElementEnd");
                mPreviewPager.notifyDataSetChanged();
            }

            @Override
            public void onRejectSharedElements(List<View> rejectedSharedElements) {
                super.onRejectSharedElements(rejectedSharedElements);
                KLog.d("zzz", "onRejectSharedElements");

            }


            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                KLog.d("zzz", "onMapSharedElements");

            }

            @Override
            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
                KLog.d("zzz", "onCaptureSharedElementSnapshot");

                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);


            }

            @Override
            public View onCreateSnapshotView(Context context, Parcelable snapshot) {
                KLog.d("zzz", "onCreateSnapshotView");

                return super.onCreateSnapshotView(context, snapshot);
            }

            @Override
            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
                KLog.d("zzz", "onSharedElementsArrived");

            }
        });
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
        initPreview();
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
        ViewCompat.setTransitionName(this.mPreviewPager, getString(R.string.share_element_picture) + mPosition);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.anim_activity_right_exit);
    }
}
