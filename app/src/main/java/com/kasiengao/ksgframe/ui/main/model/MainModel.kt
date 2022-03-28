package com.kasiengao.ksgframe.ui.main.model

import com.google.gson.reflect.TypeToken
import com.kaisengao.base.util.CommonUtil
import com.kaisengao.mvvm.base.model.BaseModel
import com.kaisengao.retrofit.RxCompose
import com.kaisengao.retrofit.factory.GsonBuilderFactory
import com.kasiengao.ksgframe.factory.AppFactory
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.kasiengao.ksgframe.ui.trainee.mvvm.Injection
import com.kasiengao.ksgframe.ui.trainee.mvvm.data.source.DataRepository
import io.reactivex.*
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * @ClassName: MainModel
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/26 20:59
 * @Description:
 */
class MainModel : BaseModel<DataRepository>(Injection.provideDataRepository()) {

    private val mRandom: Random by lazy { Random() }

    private val mUsers = mutableListOf(
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/63d0f703918fa0ec08fa7d63ccc54eee3d6d55fb6d18?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "蒙奇·D·路飞",
            "草帽一伙的船长，绰号草帽小子"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/03087bf40ad162d9f2d3754cfb8dbeec8a136327671a?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "罗罗诺亚·索隆",
            "草帽一伙的战斗员，绰号海贼猎人"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/9d82d158ccbf6c81800abeb9566ca63533fa828bea1d?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "娜美",
            "草帽一伙的航海士，绰号小贼猫"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/37d12f2eb9389b504fc254ff6f67f2dde71190efc059?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "乌索普",
            "草帽一伙的狙击手，绰号狙击之王、GOD·乌索普"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/c995d143ad4bd11373f06867b0fdb30f4bfbfbedaf11?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "山治",
            "草帽一伙的厨师，绰号黑足"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/32fa828ba61ea8d3fd1feea97d58274e251f95caf259?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "托尼托尼·乔巴",
            "草帽一伙的船医，绰号爱吃棉花糖的乔巴"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/91ef76c6a7efce1b9d16775c4503e4deb48f8c54cf50?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "妮可·罗宾",
            "草帽一伙的考古学家，绰号恶魔之子"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/d6ca7bcb0a46f21fbe092afd1c767c600c3387440058?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "弗兰奇",
            "草帽一伙的船匠，绰号铁人·弗兰奇"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/8c1001e93901213fb80e0172beb521d12f2eb9383f58?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "布鲁克",
            "草帽一伙的音乐家，绰号鼻呗·布鲁克、灵魂之王"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/962bd40735fae6cd7b897ecfe5e1182442a7d933a158?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "甚平",
            "草帽一伙的舵手，绰号海侠·甚平"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/e850352ac65c10385343a56d58438413b07eca80235b?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "前进·梅利号",
            "草帽一伙的第一艘正式的海贼船"
        ),
    )

    /**
     * 请求 视频列表
     */
    fun requestVideos(): Observable<ArrayList<VideoBean>> {
        // 模拟请求
        return Observable
            .create(ObservableOnSubscribe<ArrayList<VideoBean>> { emitter ->
                try {
                    // 数据
                    val json =
                        CommonUtil.getAssetsJson(AppFactory.application(), "VideosJson.json")
                    // 解析
                    val type = object : TypeToken<ArrayList<VideoBean>>() {}.type
                    val videos: ArrayList<VideoBean> =
                        GsonBuilderFactory.getInstance().fromJson(json, type)
                    // 随机排序
                    videos.shuffle()
                    // 设置假头像与昵称
                    for (video in videos) {
                        val infoBean = mUsers[mRandom.nextInt(mUsers.size)]
                        video.avatar = infoBean.avatar
                        video.nickname = infoBean.nickname
                        video.profile = infoBean.profile
                        mRandom.nextInt(10000).also {
                            video.setPraise(if (it < 100) 0 else it)
                        }
                        mRandom.nextInt(500).also {
                            video.setComment(if (it < 50) 0 else it)
                        }
                        mRandom.nextInt(300).also {
                            video.setShare(if (it < 30) 0 else it)
                        }
                    }
                    // Success
                    emitter.onNext(videos)
                    emitter.onComplete()
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onError(Exception("出现错误啦!!!"))
                }
            })
            .delay(1000, TimeUnit.MILLISECONDS)
            .compose(RxCompose.applySchedulers())
    }
}