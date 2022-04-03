package com.kasiengao.ksgframe.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.ui.dialog.adapter.ShareAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ShareDialog
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/2 16:19
 * @Description: 分享Dialog
 */
public class ShareDialog extends Dialog {

    public ShareDialog(@NonNull Context context) {
        super(context);
        // init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_share);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);

            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // 动画
            window.setWindowAnimations(android.R.style.Animation_Activity);
            // dialog背景颜色
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        // Init ShareAdapter
        this.initShareAdapter();
        // Cancel
        this.findViewById(R.id.dialog_cancel).setOnClickListener(v -> dismiss());
    }

    /**
     * Init ShareAdapter
     */
    private void initShareAdapter() {
        // Adapter
        ShareAdapter adapter = new ShareAdapter();
        // Recycler
        RecyclerView recycler = findViewById(R.id.dialog_shares);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recycler.setAdapter(adapter);
        // Data
        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        strings.add("5");
        adapter.setList(strings);
    }
}