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

    private int space = 0;

    private int mPos;

    public StaggeredItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NotNull View view, RecyclerView parent, @NotNull RecyclerView.State state) {
        outRect.top = space;
        // 该View在整个RecyclerView中位置。
        mPos = parent.getChildAdapterPosition(view);
        // 两列的左边一列
        if (mPos % 2 == 0) {
            outRect.left = space;
            outRect.right = space / 2;
        }
        // 两列的右边一列
        if (mPos % 2 == 1) {
            outRect.left = space / 2;
            outRect.right = space;
        }
    }
}
