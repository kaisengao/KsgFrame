package com.kasiengao.ksgframe.ui.trainee.staggered;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: StaggeredItemDecoration
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 11:53
 * @Description:
 */
public class StaggeredItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public StaggeredItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NotNull View view, RecyclerView parent, @NotNull RecyclerView.State state) {
        outRect.top = mSpace;
        // 该View在整个RecyclerView中位置。
        int position = parent.getChildAdapterPosition(view);
        // 两列的左边一列
        if (position % 2 == 0) {
            outRect.left = mSpace;
            outRect.right = mSpace / 2;
        }
        // 两列的右边一列
        if (position % 2 == 1) {
            outRect.left = mSpace / 2;
            outRect.right = mSpace;
        }
    }
}
