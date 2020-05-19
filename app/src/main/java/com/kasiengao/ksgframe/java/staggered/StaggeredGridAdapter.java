package com.kasiengao.ksgframe.java.staggered;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.base.util.GlideUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.element.PreviewBean;
import com.kasiengao.ksgframe.java.element.ShareElementActivity;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: StaggeredGridAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:59
 * @Description: 瀑布流 Adapter
 */
public class StaggeredGridAdapter extends BaseQuickAdapter<StaggeredGridBean, StaggeredGridAdapter.ViewHolder> implements LoadMoreModule {

    private AppCompatActivity mCompatActivity;

    public StaggeredGridAdapter(AppCompatActivity compatActivity) {
        super(R.layout.item_staggered_grid);
        this.mCompatActivity = compatActivity;
        super.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    @Override
    protected void convert(@NotNull ViewHolder holder, StaggeredGridBean item) {

        if (item.mPreviewBeans != null && !item.mPreviewBeans.isEmpty()) {
            // 默认显示第一张图
            PreviewBean previewBean = item.mPreviewBeans.get(0);
            // 获取item宽度，计算图片等比例缩放后的高度，为imageView设置参数
            ViewGroup.LayoutParams layoutParams = holder.mPicture.getLayoutParams();
            int itemWidth = (int) (DensityUtil.getWidthInPx(getContext())) / 2;
            float scale = (itemWidth + 0f) / previewBean.mWidth;
            int itemHeight = (int) (previewBean.mHeight * scale);
            // 重新赋值
            layoutParams.width = itemWidth;
            layoutParams.height = itemHeight;
            holder.mPicture.setLayoutParams(layoutParams);
            // Load
            GlideUtil.loadImage(getContext(), previewBean.mMediaUrl, itemWidth, itemHeight, holder.mPicture);
        }

        holder.mName.setText(item.mName);
        holder.mContent.setText(item.mContent);

        ViewCompat.setTransitionName(holder.mPicture, getContext().getString(R.string.share_element_picture) + holder.getLayoutPosition());
    }

    class ViewHolder extends BaseViewHolder {

        private final AppCompatImageView mPicture;
        private final AppCompatTextView mName;
        private final AppCompatTextView mContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mPicture = itemView.findViewById(R.id.item_staggered_grid_picture);
            this.mName = itemView.findViewById(R.id.item_staggered_grid_name);
            this.mContent = itemView.findViewById(R.id.item_staggered_grid_content);

            itemView.setOnClickListener(v -> {
                int layoutPosition = getLayoutPosition();
                StaggeredGridBean gridBean = getData().get(layoutPosition);
                // ShareElement
                Intent intent = new Intent(mCompatActivity, ShareElementActivity.class);
                intent.putExtra(ShareElementActivity.DATA, gridBean);
                intent.putExtra(ShareElementActivity.POSITION, layoutPosition);
                ActivityOptionsCompat activityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(mCompatActivity, mPicture, getContext().getString(R.string.share_element_picture) + layoutPosition);
                ActivityCompat.startActivity(mCompatActivity, intent, activityOptionsCompat.toBundle());
            });
        }
    }
}
