package com.kasiengao.ksgframe.java.element;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.base.util.GlideUtil;
import com.kasiengao.ksgframe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PreviewPager
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/20 10:43
 * @Description: 自适应 Banner
 */
public class PreviewPager<T extends IPreviewPagerParams> extends FrameLayout implements ViewPager.OnPageChangeListener {

    private int mNormalHeight = 0;

    private List<T> mMediaList;

    private ViewPager mViewPager;

    private AppCompatImageView mPreview;

    private AppCompatTextView mViewPagerCount;

    private PreviewPagerAdapter<T> mPagerAdapter;

    public PreviewPager(@NonNull Context context) {
        this(context, null);
    }

    public PreviewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
        // Init View
        this.initView();
    }

    /**
     * Init
     */
    private void init() {

    }

    /**
     * Init View
     */
    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_preview_pager, this, true);
        this.mPreview = inflate.findViewById(R.id.layout_preview);
        this.mViewPager = inflate.findViewById(R.id.layout_preview_pager);
        this.mViewPager.addOnPageChangeListener(this);
        this.mViewPager.setAdapter(mPagerAdapter = new PreviewPagerAdapter<>());
        this.mViewPagerCount = inflate.findViewById(R.id.layout_preview_pager_count);
    }

    /**
     * 赋值 媒体数据列表
     *
     * @param mediaList mediaList
     */
    public void setMediaList(List<T> mediaList) {
        this.mMediaList = mediaList == null ? this.mMediaList = new ArrayList<>() : mediaList;
        // 默认以第一张图的宽高为准
        this.setLayoutParams(this.mMediaList.get(0));
        // 配置 ViewPager 页数
        this.setCurrentCount();
    }

    /**
     * 配置 ViewPager 的宽高
     *
     * @param pagerParams pagerParams
     */
    private void setLayoutParams(IPreviewPagerParams pagerParams) {
        // 等比例缩放宽高
        int[] screenSize = DensityUtil.scaleScreenSize(getContext(), pagerParams.getWidth(), pagerParams.getHeight(), 1);
        this.mNormalHeight = screenSize[1];
        this.getLayoutParams().width = screenSize[0];
        this.getLayoutParams().height = screenSize[1];
        this.setLayoutParams(getLayoutParams());

        // 显示预览图
        ViewGroup.LayoutParams layoutParams = this.mPreview.getLayoutParams();
        layoutParams.width = screenSize[0];
        layoutParams.height = screenSize[1];
        this.mPreview.setLayoutParams(layoutParams);

        GlideUtil.loadImage(getContext(), pagerParams.getMediaUrl(), this.mPreview);
    }

    /**
     * 配置 ViewPager 页数
     */
    private void setCurrentCount() {
        this.mViewPagerCount.setText(String.format(getContext().getString(R.string.preview_count), (this.mViewPager.getCurrentItem() + 1), this.mMediaList.size()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 验证 最后一个item不作处理
        if ((mPagerAdapter.getCount() - 1) == position) {
            return;
        }
        // 高度数据
        int[] itemHeight = mPagerAdapter.getItemHeight();
        // 动态配置ViewPager高度达到自适应效果
        getLayoutParams().height = (int) ((itemHeight[position] == 0 ? mNormalHeight : itemHeight[position]) * (1 - positionOffset) +
                (itemHeight[position + 1] == 0 ? mNormalHeight : itemHeight[position + 1]) * positionOffset);
        setLayoutParams(getLayoutParams());
    }

    @Override
    public void onPageSelected(int position) {
        // 配置 ViewPager 页数
        this.setCurrentCount();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 刷新 Adapter
     */
    public void notifyDataSetChanged() {
        this.mPagerAdapter.setMediaList(this.mMediaList);

        this.mViewPager.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                // 隐藏预览图
                mPreview.setVisibility(View.GONE);
//                mViewPager.getViewTreeObserver().removeOnDrawListener(this);
            }
        });
    }

    /**
     * Adapter
     *
     * @param <T> {@link IPreviewPagerParams}
     */
    static class PreviewPagerAdapter<T extends IPreviewPagerParams> extends PagerAdapter {

        private int[] mItemHeight;

        private List<T> mMediaList;

        void setMediaList(List<T> mediaList) {
            this.mMediaList = mediaList;
            // ItemHeight 高度数组
            this.mItemHeight = null;
            this.mItemHeight = new int[this.getCount()];
            // notifyData
            this.notifyDataSetChanged();
        }

        int[] getItemHeight() {
            return this.mItemHeight;
        }

        @Override
        public int getCount() {
            return this.mMediaList == null ? 0 : this.mMediaList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // Data
            IPreviewPagerParams pagerParams = this.mMediaList.get(position);
            // RootView
            FrameLayout itemView = new FrameLayout(container.getContext());
            container.addView(itemView);
            // AddItemView
            this.addItemView(pagerParams, position, itemView);
            // Return
            return itemView;
        }

        private void addItemView(IPreviewPagerParams pagerParams, int position, FrameLayout rooView) {
            // 类型区分
            switch (pagerParams.getMediaType()) {
                case "image":
                    AppCompatImageView imageView = new AppCompatImageView(rooView.getContext());
                    // Add
                    rooView.addView(imageView);
                    // 等比例缩放宽高
                    int[] screenSize = DensityUtil.scaleScreenSize(rooView.getContext(), pagerParams.getWidth(), pagerParams.getHeight(), 1);
                    // 存储高度
                    this.mItemHeight[position] = screenSize[1];
                    // LoadImage
                    GlideUtil.loadImage(rooView.getContext(), pagerParams.getMediaUrl(), imageView);
                    break;
                case "video":
                    break;
                default:
                    break;
            }
        }
    }
}
