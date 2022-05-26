package com.kaisengao.ksgframe.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.ui.main.bean.VideoBean
import com.kaisengao.ksgframe.ui.main.model.MainModel
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel
import com.kaisengao.retrofit.observer.BaseRxObserver
import com.kuaishou.akdanmaku.data.DanmakuItemData
import io.reactivex.disposables.Disposable

/**
 * @ClassName: MainViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 11:46
 * @Description:
 */
class MainViewModel(application: Application) : ToolbarViewModel(application) {

    val mRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    val mVideos: MutableLiveData<List<VideoBean>> by lazy { MutableLiveData() }

    val mDanmakuData: MutableLiveData<List<DanmakuItemData>> by lazy { MutableLiveData() }

    private val mModel: MainModel by lazy { MainModel() }

    /**
     * Init Toolbar
     */
    override fun initToolbar() {
        this.setToolbarTitle(" ")
        this.setNavigationIcon(R.drawable.ic_toolbar_menu)
    }

    /**
     * 请求 视频列表
     */
    fun requestVideos() {
        this.mModel
            .requestVideos()
            .doOnSubscribe(this)
            .subscribe(object :
                BaseRxObserver<List<VideoBean>>() {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    mRefreshing.value = true
                }

                override fun onResult(videos: List<VideoBean>) {
                    mVideos.value = videos
                    mRefreshing.value = false
                }

                override fun onError(message: String?) {
                    super.onError(message)
                    mRefreshing.value = false
                }
            })
    }

    /**
     * 请求 弹幕数据
     */
    fun requestDanmakuData() {
        this.mModel
            .requestDanmakuData()
            .doOnSubscribe(this)
            .subscribe(object :
                BaseRxObserver<List<DanmakuItemData>>() {
                override fun onResult(data: List<DanmakuItemData>) {
                    mDanmakuData.value = data
                }
            })
    }
}