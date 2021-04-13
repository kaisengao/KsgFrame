package com.kasiengao.ksgframe.kt.camera

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kaisengao.base.util.TimeUtil
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel
import com.kaisengao.retrofit.RxCompose
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @ClassName: CameraViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/6 10:02
 * @Description:
 */
class CameraViewModel(application: Application) : ToolbarViewModel(application) {

    private var mRecording: MutableLiveData<Boolean>? = null

    private var mRecordTime: MutableLiveData<String>? = null

    private var mRecordDisposable: Disposable? = null

    /**
     * 开始 录像时长计时
     */
    fun startRecordTime() {
        this.stopRecordTime()
        // 标记正在录像
        this.getRecording()!!.value = true
        // 计时
        Observable
            .interval(1000, TimeUnit.MILLISECONDS)
            .doOnSubscribe(this)
            .compose(RxCompose.applySchedulers())
            .subscribe(object : Observer<Long> {
                override fun onSubscribe(disposable: Disposable) {
                    mRecordDisposable = disposable
                }

                override fun onNext(milliseconds: Long) {
                    // 已录制时长
                    getRecordTime()!!.value =
                        String.format("已录制%s", TimeUtil.getTimeFormatText(milliseconds * 1000))
                }

                override fun onError(e: Throwable) {
                    onComplete()
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 停止 录像时长计时
     */
    fun stopRecordTime() {
        if (mRecordDisposable != null) {
            this.mRecordDisposable!!.dispose()
            this.mRecordDisposable = null
            // 标记停止录像
            this.getRecording()!!.value = false
        }
    }

    fun getRecording(): MutableLiveData<Boolean?>? {
        return createLiveData(mRecording).also { mRecording = it }
    }

    fun getRecordTime(): MutableLiveData<String?>? {
        return createLiveData(mRecordTime).also { mRecordTime = it }
    }

    override fun onCleared() {
        super.onCleared()
        // 停止 录像时长计时
        this.stopRecordTime()
    }
}