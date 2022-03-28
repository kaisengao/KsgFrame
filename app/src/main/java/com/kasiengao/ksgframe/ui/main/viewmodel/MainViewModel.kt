package com.kasiengao.ksgframe.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel
import com.kaisengao.retrofit.observer.dialog.BaseDialogObserver
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.kasiengao.ksgframe.ui.main.model.MainModel

/**
 * @ClassName: MainViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 11:46
 * @Description:
 */
class MainViewModel(application: Application) : ToolbarViewModel(application) {

    val mVideos: MutableLiveData<ArrayList<VideoBean>> by lazy { MutableLiveData() }

    private val mModel: MainModel by lazy { MainModel() }

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
            .subscribe(object : BaseDialogObserver<ArrayList<VideoBean>>(getApplication()) {
                override fun onResult(videos: ArrayList<VideoBean>) {
                    mVideos.value = videos
                }
            })
    }

    companion object {

    }
}