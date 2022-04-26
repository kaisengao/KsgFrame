package com.ksg.ksgplayer.renderer.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.ksg.ksgplayer.renderer.filter.base.GLOesFilter;

/**
 * @ClassName: GLPIPFilter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 15:06
 * @Description: 画中画 Filter
 */
public class GLPIPFilter extends GLOesFilter {

    private final GLOesFilter mOesFilter;

    public GLPIPFilter(Context context,GLOesFilter oesFilter) {
        super(context);
        this.setBindFbo(true);
        this.mOesFilter = oesFilter;
    }

    /**
     * 绘制准备
     */
    @Override
    public boolean onReadyToDraw() {
        return true;
    }
    /**
     * 绘制之前
     */
    @Override
    public void onDrawPre() {
    }

    @Override
    public void onSetOtherData() {
        GLES20.glUniformMatrix4fv(mOesFilter.getMatrixLocation(), 1, false, mOesFilter.getMVPMatrix(), 0);
        GLES20.glUniformMatrix4fv(mOesFilter.getOesMatrixLocation(), 1, false, mOesFilter.getOesMatrix(), 0);
    }
}
