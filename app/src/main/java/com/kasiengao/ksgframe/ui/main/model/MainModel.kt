package com.kasiengao.ksgframe.ui.main.model

import com.kaisengao.base.util.CommonUtil
import com.kaisengao.mvvm.base.model.BaseModel
import com.kaisengao.retrofit.RxCompose
import com.kaisengao.retrofit.factory.GsonBuilderFactory
import com.kasiengao.ksgframe.factory.AppFactory
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.kasiengao.ksgframe.ui.trainee.mvvm.Injection
import com.kasiengao.ksgframe.ui.trainee.mvvm.data.source.DataRepository
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import org.json.JSONException
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * @ClassName: MainModel
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/26 20:59
 * @Description:
 */
class MainModel : BaseModel<DataRepository>(Injection.provideDataRepository()) {

    /**
     * 请求 视频列表
     */
    fun requestVideos(): Observable<VideoBean> {
        // 模拟请求
        return Observable
            .create(ObservableOnSubscribe<VideoBean> { emitter ->
                try {
                    // 数据
                    val json =
                        CommonUtil.getAssetsJson(AppFactory.application(), "VideosJson.json")
                    // 解析
                    val videoBean =
                        GsonBuilderFactory.getInstance().fromJson(json, VideoBean::class.java)
                    // Success
                    emitter.onNext(videoBean)
                    emitter.onComplete()
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onError(Exception("出现错误啦!!!"))
                }
            })
            .delay(3000, TimeUnit.MILLISECONDS)
            .compose(RxCompose.applySchedulers())
    }
}