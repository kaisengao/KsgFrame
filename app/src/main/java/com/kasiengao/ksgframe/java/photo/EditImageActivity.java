package com.kasiengao.ksgframe.java.photo;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.ActivityEditImageBinding;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.ViewType;

/**
 * @ClassName: EditImageActivity
 * @Author: KaiSenGao
 * @CreateDate: 2021/10/19 11:32
 * @Description: 图片编辑
 */
public class EditImageActivity extends BaseVmActivity<ActivityEditImageBinding, EditImageViewModel> implements OnPhotoEditorListener {
    private static final String TAG = EditImageActivity.class.getSimpleName();

    private PhotoEditor mPhotoEditor;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_image;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Set BaseMap
        this.mBinding.photoEditor.getSource().setImageResource(R.drawable.ic_fish);
        // Init PhotoEditor
        this.mPhotoEditor = new PhotoEditor.Builder(this, mBinding.photoEditor)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)·
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk
        this.mPhotoEditor.setOnPhotoEditorListener(this);
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onTouchSourceImage(MotionEvent event) {
        Log.d(TAG, "onTouchView() called with: event = [" + event + "]");
    }
}