package com.kaisengao.uvccamera;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

/**
 * @ClassName: CameraProcessLib
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/2 10:32
 * @Description:
 */
public class CameraProcessLib {

    static {
        System.loadLibrary("camera_process_lib");
    }

    public static native int prepareUsbCamera(int width, int height);

    public static native void processUsbCamera();

    public static native void releaseUsbCamera();

    public static native void pixelToBmp(@NonNull Bitmap bitmap);
}
