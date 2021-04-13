package com.kaisengao.uvccamera.exe;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName: ExeCommand
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/13 11:40
 * @Description: 执行命令
 */
public class ExeCommand {
    // shell进程
    private Process mProcess;
    // 对应进程的3个流
    private BufferedReader mSuccessResult;
    private BufferedReader mErrorResult;
    // 是否同步，true：run会一直阻塞至完成或超时。false：run会立刻返回
    private final boolean mBSynchronous;
    // 表示shell进程是否还在运行
    private boolean mBRunning = false;
    // 同步锁
    private final ReadWriteLock mWriteLock = new ReentrantReadWriteLock();
    // 保存执行结果
    private final StringBuffer mResult = new StringBuffer();

    /**
     * 构造函数
     *
     * @param synchronous true：同步，false：异步
     */
    public ExeCommand(boolean synchronous) {
        this.mBSynchronous = synchronous;
    }

    /**
     * 默认构造函数，默认是同步执行
     */
    public ExeCommand() {
        this.mBSynchronous = true;
    }

    /**
     * 还没开始执行，和已经执行完成 这两种情况都返回false
     *
     * @return 是否正在执行
     */
    public boolean isRunning() {
        return mBRunning;
    }

    /**
     * @return 返回执行结果
     */
    public String getResult() {
        Lock readLock = mWriteLock.readLock();
        readLock.lock();
        try {
            Log.i("auto", "getResult = " + mResult);
            return new String(mResult);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 执行命令
     *
     * @param command eg: cat /sdcard/test.txt
     *                路径最好不要是自己拼写的路径，最好是通过方法获取的路径
     *                example：Environment.getExternalStorageDirectory()
     * @param maxTime 最大等待时间 (ms)
     * @return this
     */
    public ExeCommand run(String command, final int maxTime) {
        Log.i("auto", "run command:" + command + ",maxTime:" + maxTime);
        if (command == null || command.length() == 0) {
            return this;
        }
        try {
            this.mProcess = Runtime.getRuntime().exec("su");
        } catch (Exception e) {
            return this;
        }
        this.mBRunning = true;
        this.mSuccessResult = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
        this.mErrorResult = new BufferedReader(new InputStreamReader(mProcess.getErrorStream()));
        DataOutputStream os = new DataOutputStream(mProcess.getOutputStream());
        try {
            // 向su写入要执行的命令
            os.write(command.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            // 如果等待时间设置为非正，就不开启超时关闭功能
            if (maxTime > 0) {
                // 超时就关闭进程
                new Thread(() -> {
                    try {
                        Thread.sleep(maxTime);
                    } catch (Exception ignored) {
                    }
                    try {
                        int ret = mProcess.exitValue();
                        Log.i("auto", "exitValue Stream over" + ret);
                    } catch (IllegalThreadStateException e) {
                        Log.i("auto", "take maxTime,forced to destroy process");
                        mProcess.destroy();
                    }
                }).start();
            }
            // 开一个线程来处理input流
            final Thread t1 = new Thread(() -> {
                String line;
                Lock writeLock = mWriteLock.writeLock();
                try {
                    while ((line = mSuccessResult.readLine()) != null) {
                        line += "\n";
                        writeLock.lock();
                        mResult.append(line);
                        writeLock.unlock();
                    }
                } catch (Exception e) {
                    Log.i("auto", "read InputStream exception:" + e.toString());
                } finally {
                    try {
                        mSuccessResult.close();
                        Log.i("auto", "read InputStream over");
                    } catch (Exception e) {
                        Log.i("auto", "close InputStream exception:" + e.toString());
                    }
                }
            });
            t1.start();
            // 开一个线程来处理error流
            final Thread t2 = new Thread(() -> {
                String line;
                Lock writeLock = mWriteLock.writeLock();
                try {
                    while ((line = mErrorResult.readLine()) != null) {
                        line += "\n";
                        writeLock.lock();
                        mResult.append(line);
                        writeLock.unlock();
                    }
                } catch (Exception e) {
                    Log.i("auto", "read ErrorStream exception:" + e.toString());
                } finally {
                    try {
                        mErrorResult.close();
                        Log.i("auto", "read ErrorStream over");
                    } catch (Exception e) {
                        Log.i("auto", "read ErrorStream exception:" + e.toString());
                    }
                }
            });
            t2.start();

            Thread t3 = new Thread(() -> {
                try {
                    //等待执行完毕
                    t1.join();
                    t2.join();
                    mProcess.waitFor();
                } catch (Exception ignored) {

                } finally {
                    mBRunning = false;
                    Log.i("auto", "run command process end");
                }
            });
            t3.start();

            if (mBSynchronous) {
                Log.i("auto", "run is go to end");
                t3.join();
                Log.i("auto", "run is end");
            }
        } catch (Exception e) {
            Log.i("auto", "run command process exception:" + e.toString());
        }
        return this;
    }
}