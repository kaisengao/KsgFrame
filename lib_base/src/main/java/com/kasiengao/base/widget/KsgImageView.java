package com.kasiengao.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.kasiengao.base.configure.ThreadPool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName: KsgImageView
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/28 0:51
 * @Description: Demo专用 自带网络请求ImageView
 */
public class KsgImageView extends AppCompatImageView {

    public KsgImageView(Context context) {
        super(context);
    }

    public KsgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置网络图片
     *
     * @param path 图片地址
     */
    public void setImageUrl(final String path) {
        // 开启一个线程用于联网
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //把传过来的路径转成URL
                    URL url = new URL(path);
                    //获取连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //使用GET方法访问网络
                    connection.setRequestMethod("GET");
                    //超时时间为10秒
                    connection.setConnectTimeout(10000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        // 使用工厂把网络的输入流生产Bitmap
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // 子线程不能操作UI，通过主线程设置图片
                        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                setImageBitmap(bitmap);
                            }
                        });
                    } else {
                        // 服务启发生错误
                        Toast.makeText(getContext(), "服务器发生错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // 网络连接错误
                    Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
