package com.kasiengao.ksgframe.ui.main.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.kaisengao.base.state.LoadingState
import com.kaisengao.base.util.SnackbarUtil
import com.kaisengao.mvvm.binding.command.BindingParamImp
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel
import com.kaisengao.retrofit.observer.mvvm.BaseLoadPageObserver
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.load.MainVideoLoad
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.kasiengao.ksgframe.ui.main.model.MainModel

/**
 * @ClassName: MainViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 11:46
 * @Description:
 */
class MainViewModel(application: Application) : ToolbarViewModel(application) {

    val mVideosLoad: MutableLiveData<LoadingState> by lazy { MutableLiveData() }

    val mVideos: MutableLiveData<ArrayList<VideoBean>> by lazy { MutableLiveData() }

    val mVideo: MutableLiveData<VideoBean> by lazy { MutableLiveData() }

    private val mModel: MainModel by lazy { MainModel() }

    /**
     * Reload
     */
    val onVideosReloadImp: BindingParamImp<Any> = BindingParamImp {
        // 请求 视频列表
        this.requestVideos()
    }

    /**
     * Init Toolbar
     */
    override fun initToolbar() {
        this.setToolbarTitle(getApplication<Application>().getString(R.string.title))
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
                BaseLoadPageObserver<ArrayList<VideoBean>>(getApplication(), mVideosLoad) {
                override fun onResult(videos: ArrayList<VideoBean>) {
                    mVideos.value = videos
                }
            }.setLoadView(MainVideoLoad::class.java))
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