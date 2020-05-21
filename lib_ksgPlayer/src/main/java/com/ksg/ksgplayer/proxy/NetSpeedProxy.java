package com.ksg.ksgplayer.proxy;

import android.content.Context;
import android.os.Looper;

import com.ksg.ksgplayer.config.KsgWeakHandler;
import com.ksg.ksgplayer.listener.OnNetSpeedListener;
import com.ksg.ksgplayer.helper.NetSpeedHelper;

/**
 * @author kaisengao
 * @create: 2019/2/27 15:20
 * @describe: 网络速度 代理
 */
public class NetSpeedProxy {

    private Context mContext;
    /**
     * 间隔时间
     */
    private int mCounterInterval;

    /**
     * 代理的状态,默认使用
     */
    private boolean mUseProxy = true;

    /**
     * 弱引用 handler
     */
    private KsgWeakHandler mHandler = new KsgWeakHandler(Looper.getMainLooper());

    private NetSpeedHelper mSpeedUtils;

    private OnNetSpeedListener mOnNetSpeedListener;

    public NetSpeedProxy(Context context, int counterInterval) {
        this.mContext = context;
        this.mCounterInterval = counterInterval;

        mSpeedUtils = new NetSpeedHelper();
    }

    public void setOnNetSpeedListener(OnNetSpeedListener onNetSpeedListener) {
        mOnNetSpeedListener = onNetSpeedListener;
    }

    /**
     * 设置代理状态
     */
    public void setUseProxy(boolean useProxy) {
        this.mUseProxy = useProxy;
        if (!useProxy) {
            cancel();
        } else {
            start();
        }
    }

    /**
     * 开始获取网速
     */
    public void start() {
        removeMessage();
        mHandler.post(mRunnable);
    }

    /**
     * 轮询获取网速
     */
    private void loopNext() {
        removeMessage();
        mHandler.postDelayed(mRunnable, mCounterInterval);
    }

    /**
     * 取消获取网速
     */
    public void cancel() {
        removeMessage();
    }

    /**
     * 取消发送handler
     */
    private void removeMessage() {
        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * 开启线程 获取网速
     */
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mUseProxy) {
                return;
            }

            if (mOnNetSpeedListener != null) {
                String netSpeed = mSpeedUtils.getNetSpeed(mContext.getApplicationInfo().uid);
                mOnNetSpeedListener.onNetSpeed(netSpeed);
            }

            loopNext();
        }
    };
}
