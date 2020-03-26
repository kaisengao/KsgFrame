package com.kasiengao.base.configure;

import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @ClassName: ThreadPool
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:30
 * @Description: ThreadPool
 */
public class ThreadPool {

    private int mCoreSize;

    private int mQueueSize;

    private ArrayBlockingQueue<Runnable> mBlockQueue;

    private ThreadPoolExecutor mThreadPool;

    private ThreadPool() {
        this.mCoreSize = getCoresNumbers();
        this.mQueueSize = this.mCoreSize * 32;
        this.init();
    }

    public ThreadPool(int coreSize, int queueSize) {
        this.mCoreSize = coreSize;
        this.mQueueSize = queueSize;
        this.init();
    }

    private void init() {
        this.mCoreSize = Math.min(4, this.mCoreSize);
        this.mBlockQueue = new ArrayBlockingQueue<>(this.mQueueSize);

        this.mThreadPool = new ThreadPoolExecutor(
                this.mCoreSize, this.mCoreSize * 2, 8L,
                TimeUnit.SECONDS,
                this.mBlockQueue,
                new ThreadFactory() {

                    AtomicInteger threadId = new AtomicInteger(1);

                    public Thread newThread(@NonNull Runnable runnable) {
                        return new Thread(runnable, "IMI-Thread-Pool-" + this.threadId.getAndDecrement());
                    }

                },
                new RejectedExecutionHandler() {
                    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

                        Log.e("ThreadPool", "rejectedExecution");

                        Log.e("ThreadPool", ThreadPool.this.mBlockQueue.size() + "");
                    }
                });
    }

    private Future<?> submit(Runnable runnable) {
        return this.mThreadPool.submit(runnable);
    }

    private static int getCoresNumbers() {

        int cpuCores = 0;
        try {
            File dir = new File("/sys/devices/system/cpu/");

            class CpuFilter implements FileFilter {
                private CpuFilter() {
                }

                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]+", pathname.getName());
                }
            }

            File[] files = dir.listFiles(new CpuFilter());
            cpuCores = files.length;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if (cpuCores < 1) {
            cpuCores = Runtime.getRuntime().availableProcessors();
        }

        if (cpuCores < 1) {
            cpuCores = 1;
        }

        Log.i("ThreadPool", "CPU cores: " + cpuCores);

        return cpuCores;
    }

    public static class MainThreadHandler {

        WeakHandler mHandler;

        private MainThreadHandler() {
            this.mHandler = new WeakHandler(Looper.getMainLooper());
        }

        public static MainThreadHandler getInstance() {
            return SingletonHolder.INSTANCE;
        }

        public void post(Runnable runnable) {
            this.mHandler.post(runnable);
        }

        public void post(Runnable runnable, long delayMillis) {
            this.mHandler.postDelayed(runnable, delayMillis);
        }

        public void removeCallbacks(Runnable runnable) {
            this.mHandler.removeCallbacks(runnable);
        }

        private static class SingletonHolder {
            private static final MainThreadHandler INSTANCE = new MainThreadHandler();

            private SingletonHolder() {
            }
        }
    }

    public static class DefaultThreadPool {

        ThreadPool mThreadPool;

        private DefaultThreadPool() {
            this.mThreadPool = new ThreadPool();
        }

        public static DefaultThreadPool getInstance() {
            return SingletonHolder.INSTANCE;
        }

        public Future<?> submit(Runnable runnable) {
            return this.mThreadPool.submit(runnable);
        }

        private static class SingletonHolder {
            private static final DefaultThreadPool INSTANCE = new DefaultThreadPool();

            private SingletonHolder() {
            }
        }
    }
}
