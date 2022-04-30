package com.kasiengao.ksgframe.ui.main.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.kaisengao.base.util.SnackbarUtil
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel
import com.kaisengao.retrofit.observer.BaseRxObserver
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.kasiengao.ksgframe.ui.main.model.MainModel
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

    val mVideo: MutableLiveData<VideoBean> by lazy { MutableLiveData() }

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
                BaseRxObserver<List<VideoBean>>(getApplication()) {
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
                BaseRxObserver<List<DanmakuItemData>>(getApplication()) {
                override fun onResult(data: List<DanmakuItemData>) {
                    mDanmakuData.value = data
                }
            })
    }

    /**
     * 刷新信息
     */
    fun refreshInfo(position: Int) {
        this.mVideos.value?.let {
            mVideo.value = it[position]
        }
    }

    /**
     * 关注
     */
    fun onFollow(view: View) {
        SnackbarUtil.with(view).setMessage("关注一波啊，喂！").show()
    }
}