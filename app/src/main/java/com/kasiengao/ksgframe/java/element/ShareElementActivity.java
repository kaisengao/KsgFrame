package com.kasiengao.ksgframe.java.element;

import android.view.MenuItem;

import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;

/**
 * @ClassName: ShareElement
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/18 8:20 PM
 * @Description:
 */
public class ShareElementActivity extends BaseToolbarActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_share_element;
    }


    @Override
    protected void initWindow() {
        super.initWindow();
        // Toolbar Title
        this.setTitle(R.string.share_element_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
