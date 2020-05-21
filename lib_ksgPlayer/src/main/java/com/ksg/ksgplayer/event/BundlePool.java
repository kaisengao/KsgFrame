package com.ksg.ksgplayer.event;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kaisengao
 * @create: 2019/1/7 15:52
 * @describe: 为了提高内存的性能, 包实体框架中传递, 来自包缓冲池。
 */
public class BundlePool {

    private static final int POOL_SIZE = 3;

    private static List<Bundle> mPool;

    static {
        mPool = new ArrayList<>();
        for (int i = 0; i < POOL_SIZE; i++) {
            mPool.add(new Bundle());
        }
    }

    public synchronized static Bundle obtain() {
        for (int i = 0; i < POOL_SIZE; i++) {
            if (mPool.get(i).isEmpty()) {
                return mPool.get(i);
            }
        }
        return new Bundle();
    }
}
