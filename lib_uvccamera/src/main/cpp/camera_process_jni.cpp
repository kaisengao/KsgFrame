#include "camera_process_lib.hpp"
#include "util/android_buf.hpp"

extern "C" JNIEXPORT jint JNICALL
Java_com_kaisengao_uvccamera_CameraProcessLib_prepareUsbCamera(
        JNIEnv *env, jclass type, jint width, jint height) {
    return prepareUsbCamera(width, height);
}

extern "C" JNIEXPORT void JNICALL
Java_com_kaisengao_uvccamera_CameraProcessLib_processUsbCamera
        (JNIEnv *env, jclass type) {
    processUsbCamera();
}

extern "C" JNIEXPORT void JNICALL
Java_com_kaisengao_uvccamera_CameraProcessLib_pixelToBmp(
        JNIEnv *env, jclass type, jobject bitmap) {
    pixelToBmp(env, bitmap);
}

extern "C" JNIEXPORT void JNICALL
Java_com_kaisengao_uvccamera_CameraProcessLib_releaseUsbCamera(
        JNIEnv *env, jclass type) {
    releaseUsbCamera();
}

