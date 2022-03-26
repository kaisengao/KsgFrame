package com.ksg.ksgplayer.event;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kaisengao
 * @create: 2019/1/7 15:52
 * @describe: 缓冲Bundle。
 */
public class BundlePool {

    private static final int POOL_SIZE = 3;

    private static final List<Bundle> mPool;

    static {
        mPool = new ArrayList<>();
        for (int i = 0; i < POOL_SIZE; i++) {
            mPool.add(new Bundle());
        }
    }

    public synchronized static Bundle obtain() {
        for (int i = 0; i < POOL_SIZE; i++) {
            Bundle bundle = mPool.get(i);
            if (bundle.isEmpty()) {
                return bundle;
            }
        }
        return new Bundle();
    }
}
