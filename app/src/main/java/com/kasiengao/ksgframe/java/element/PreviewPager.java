package com.kasiengao.ksgframe.java.element;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kasiengao.base.util.CommonUtil;
import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.player.KsgIjkPlayer;
import com.ksg.ksgplayer.widget.KsgAssistView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PreviewPager
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/20 10:43
 * @Description: 自适应 预览 Banner
 */
public class PreviewPager<T extends IPreviewParams> extends FrameLayout implements ViewPager.OnPageChangeListener {

    private int mNormalHeight = 0;

    private List<T> mMediaList;

    private final Lifecycle mLifecycle;

    private ViewPager mViewPager;

    private KsgAssistView mKsgAssistView;

    private AppCompatTextView mPagerCount;

    private PreviewPagerAdapter<T> mPagerAdapter;

    public PreviewPager(@NonNull Context context) {
        this(context, null);
    }

    public PreviewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init View
        this.initView();
        // Lifecycle
        this.mLifecycle = CommonUtil.scanForActivity(context).getLifecycle();
        this.mLifecycle.addObserver(mLifecycleObserver);
    }

    /**
     * Init View
     */
    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_preview_pager, this, true);
        this.mViewPager = inflate.findViewById(R.id.layout_preview_pager);
        this.mViewPager.addOnPageChangeListener(this);
        this.mViewPager.setAdapter(mPagerAdapter = new PreviewPagerAdapter<>());
        this.mPagerCount = inflate.findViewById(R.id.layout_pager_count);
    }

    /**
     * initAssistVideo
     */
    private void initAssistVideo() {
        if (this.mKsgAssistView == null) {
            this.mKsgAssistView = new KsgAssistView(getContext());
            this.mKsgAssistView.setDecoderView(new KsgIjkPlayer(getContext()));
            this.mKsgAssistView.getVideoPlayer().getKsgContainer().setBackgroundColor(Color.BLACK);
        }
    }

    /**
     * 配置 媒体数据列表
     *
     * @param mediaList mediaList
     */
    public void setMediaList(List<T> mediaList) {
        this.mMediaList = mediaList == null ? this.mMediaList = new ArrayList<>() : mediaList;
        // 默认以第一张图的宽高为准
        this.setLayoutParams(this.mMediaList.get(0));
        // 配置 ViewPager 页数
        this.setCurrentCount();
        // 配置 Data
        this.mPagerAdapter.setMediaList(this.mMediaList);
        // 默认执行
        this.mViewPager.post(() -> onPageSelected(0));
    }

    /**
     * 配置 ViewPager 的宽高
     *
     * @param pagerParams pagerParams
     */
    private void setLayoutParams(IPreviewParams pagerParams) {
        // 等比例缩放宽高
        int[] screenSize = DensityUtil.scaleScreenSize(getContext(), pagerParams.getWidth(), pagerParams.getHeight(), 1);
        this.mNormalHeight = screenSize[1];
        this.getLayoutParams().width = screenSize[0];
        this.getLayoutParams().height = screenSize[1];
        this.setLayoutParams(getLayoutParams());
    }

    /**
     * 配置 ViewPager 页数
     */
    private void setCurrentCount() {
        this.mPagerCount.setText(String.format(getContext().getString(R.string.preview_count), (this.mViewPager.getCurrentItem() + 1), this.mMediaList.size()));
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
        this.getLayoutParams().height = (int) ((itemHeight[position] == 0 ? mNormalHeight : itemHeight[position]) * (1 - positionOffset) +
                (itemHeight[position + 1] == 0 ? mNormalHeight : itemHeight[position + 1]) * positionOffset);
        this.setLayoutParams(getLayoutParams());
    }

    @Override
    public void onPageSelected(int position) {
        // 播放Item
        this.playPosition(position);
        // 配置 ViewPager 页数
        this.setCurrentCount();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 播放视频
     *
     * @param position 位置
     */
    private void playPosition(int position) {
        T pagerParams = mMediaList.get(position);
        // 不管图片还是视频只要发生改变 停止就完了
        if (this.mKsgAssistView != null) {
            this.mKsgAssistView.stop();
        }
        // 类型区分
        if ("video".equals(pagerParams.getMediaType())) {
            // container
            FrameLayout container = mViewPager.findViewWithTag(position);
            container.setVisibility(VISIBLE);
            // 初始化辅助播放器
            this.initAssistVideo();
            // 添加容器 播放
            this.mKsgAssistView.attachContainer(container, true);
            this.mKsgAssistView.setDataSource(pagerParams.getVideoUrl());
            this.mKsgAssistView.start();
            this.mKsgAssistView.getVideoPlayer().setLooping(true);
        }
    }

    /**
     * Ac 生命周期
     */
    private MyLifecycleObserver mLifecycleObserver = new MyLifecycleObserver() {

        @Override
        protected void onAcResume() {
            if (mKsgAssistView != null) {
                mKsgAssistView.resume();
            }
        }

        @Override
        protected void onAcPause() {
            if (mKsgAssistView != null) {
                mKsgAssistView.pause();
            }
        }

        @Override
        protected void onAcDestroy() {
            if (mKsgAssistView != null) {
                mKsgAssistView.destroy();
                mKsgAssistView = null;
            }
            mLifecycle.removeObserver(mLifecycleObserver);
            mLifecycleObserver = null;
            mViewPager.removeOnPageChangeListener(PreviewPager.this);
        }
    };

    /**
     * Adapter
     *
     * @param <T> {@link IPreviewParams}
     */
    static class PreviewPagerAdapter<T extends IPreviewParams> extends PagerAdapter {

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
            T pagerParams = this.mMediaList.get(position);
            // RootView
            View itemView = View.inflate(container.getContext(), R.layout.layout_preview_item, null);
            // 预览图
            SimpleDraweeView imageView = itemView.findViewById(R.id.item_preview_image);
            // 等比例缩放宽高
            int[] screenSize = DensityUtil.scaleScreenSize(container.getContext(), pagerParams.getWidth(), pagerParams.getHeight(), 1);
            // 存储高度
            this.mItemHeight[position] = screenSize[1];
            // LoadImage
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(pagerParams.getPictureUrl())
                    // 设置加载图片完成后是否直接进行播放
                    .setAutoPlayAnimations(true)
                    .build();
            imageView.setController(draweeController);
            // 播放器容器
            FrameLayout playerContainer = itemView.findViewById(R.id.item_preview_player);
            playerContainer.setTag(position);
            // ViewPager 视图添加
            container.addView(itemView);
            // Return
            return itemView;
        }
    }
}
