package com.kasiengao.ksgframe.java.staggered;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaisengao.base.util.DensityUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.preview.PreviewBean;
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

    StaggeredGridAdapter(AppCompatActivity compatActivity) {
        super(R.layout.item_staggered_grid);
        this.mCompatActivity = compatActivity;
        this.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    @Override
    protected void convert(@NotNull ViewHolder holder, StaggeredGridBean item) {

        if (item.mPreviewBeans != null && !item.mPreviewBeans.isEmpty()) {
            // 默认显示第一张图
            PreviewBean previewBean = item.mPreviewBeans.get(0);
            // 等比例缩放宽高
            int[] screenSize = DensityUtil.scaleScreenSize(getContext(), previewBean.mWidth, previewBean.mHeight, 2);
            // 更新宽高
            ViewGroup.LayoutParams layoutParams = holder.mPicture.getLayoutParams();
            layoutParams.width = screenSize[0];
            layoutParams.height = screenSize[1];
            holder.mPicture.setLayoutParams(layoutParams);
            // LoadImage
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(previewBean.mPictureUrl)
                    .setAutoPlayAnimations(true)
                    .build();
            holder.mPicture.setController(controller);
        }

        holder.mName.setText(item.mName);
        holder.mContent.setText(item.mContent);
    }

    class ViewHolder extends BaseViewHolder {

        private final SimpleDraweeView mPicture;
        private final AppCompatTextView mName;
        private final AppCompatTextView mContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mPicture = itemView.findViewById(R.id.item_staggered_grid_picture);
            this.mName = itemView.findViewById(R.id.item_staggered_grid_name);
            this.mContent = itemView.findViewById(R.id.item_staggered_grid_content);

            ViewCompat.setTransitionName(mPicture, getContext().getString(R.string.share_element_picture) + getLayoutPosition());

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
