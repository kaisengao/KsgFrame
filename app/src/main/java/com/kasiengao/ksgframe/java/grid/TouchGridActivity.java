package com.kasiengao.ksgframe.java.grid;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.ActivityTouchGridBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName: TouchGridActivity
 * @Author: KaiSenGao
 * @CreateDate: 2021/6/9 10:35
 * @Description: 拖动Grid列表
 */
public class TouchGridActivity extends BaseVmActivity<ActivityTouchGridBinding, TouchGridViewModel> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_touch_grid;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Init Adapter
        this.initAdapter();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Adapter
        TouchGridAdapter adapter = new TouchGridAdapter();
        // Recycler
        this.mBinding.dragGrid.setLayoutManager(new GridLayoutManager(this, 4));
        this.mBinding.dragGrid.setAdapter(adapter);
        // Data
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(String.valueOf((i + 1)));
        }
        adapter.setList(data);
        // 拖拽
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                final int swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // 得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                // 拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                // 交换位置
                Collections.swap(adapter.getData(), fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            /**
             * 长按选中
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.findViewById(R.id.item_grid).setEnabled(false);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开
             */
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                viewHolder.itemView.findViewById(R.id.item_grid).setEnabled(true);
                super.clearView(recyclerView, viewHolder);
            }
        });
        itemTouchHelper.attachToRecyclerView(mBinding.dragGrid);
    }
}