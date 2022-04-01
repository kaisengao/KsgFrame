package com.kasiengao.ksgframe.ui.main.model

import com.google.gson.reflect.TypeToken
import com.kaisengao.base.util.CommonUtil
import com.kaisengao.base.util.TimeUtil
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
            "草帽一伙的船长，绰号草帽小子",
            "“草帽一伙”的船长，绰号“草帽小子”。东海出身，悬赏金15亿贝里。是食用了橡胶果实的橡胶人（五老星认为这是人人果实·幻兽种·尼卡形态）。“草帽一伙”的创立者。是被称作“极恶的世代”中登陆香波地群岛的11位超新星的其中一位。同样也被世人称作“第五位海上皇帝”。梦想成为“海贼王”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/d6ca7bcb0a46f21f627635defc246b600d33aef9?x-bce-process=image/resize,m_lfit,w_1280,limit_1/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/03087bf40ad162d9f2d3754cfb8dbeec8a136327671a?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "罗罗诺亚·索隆",
            "草帽一伙的战斗员，绰号海贼猎人",
            "“草帽一伙”的战斗员，绰号“海贼猎人”。东海出身，悬赏金3亿2000万贝里。是使用三把刀战斗的三刀流剑士。“草帽一伙”第一位加入的成员。与船长路飞一样是被称作“极恶的世代”中登陆香波地群岛的11位超新星的其中一位。梦想成为“世界第一大剑豪”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/503d269759ee3d6d55fb2989c35e7a224f4a20a47012?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/9d82d158ccbf6c81800abeb9566ca63533fa828bea1d?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "娜美",
            "草帽一伙的航海士，绰号小贼猫",
            "“草帽一伙”的航海士，绰号“小贼猫”。东海出身，悬赏金6600万贝里。使用天候棒结合气象科学进行战斗。“草帽一伙”第二位加入的成员。 梦想绘制“全世界的地图”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/a044ad345982b2b796fb133c3badcbef77099be5?x-bce-process=image/resize,m_lfit,w_1280,limit_1/format,f_auto"

        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/37d12f2eb9389b504fc254ff6f67f2dde71190efc059?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "乌索普",
            "草帽一伙的狙击手，绰号狙击之王、GOD·乌索普",
            "“草帽一伙”的狙击手，绰号“狙击之王”、“GOD·乌索普”。东海出身，悬赏金2亿贝里。使用特制弹弓和植物弹药进行战斗。“草帽一伙”第三位加入的成员。梦想“成为勇敢的海上战士”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/1f178a82b9014a90f603671f7e3f2e12b31bb0514072"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/c995d143ad4bd11373f06867b0fdb30f4bfbfbedaf11?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "山治",
            "草帽一伙的厨师，绰号黑足",
            "“草帽一伙”的厨师，绰号“黑足”。北海出身，悬赏金3亿3000万贝里。使用踢技和杰尔马战斗服进行战斗。“草帽一伙”第四位加入的成员。梦想“找到传说中的海域·All Blue”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/5ab5c9ea15ce36d3d5391311a1ba2d87e950352a1bf2?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/32fa828ba61ea8d3fd1feea97d58274e251f95caf259?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "托尼托尼·乔巴",
            "草帽一伙的船医，绰号爱吃棉花糖的乔巴",
            "“草帽一伙”的船医，绰号“爱吃棉花糖的乔巴”。伟大航路出身，悬赏金100贝里。是食用了人人果实的驯鹿。“草帽一伙”第五位加入的成员。梦想“成为万能药”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/d01373f082025aafff4ec0fff1edab64034f1a81?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/91ef76c6a7efce1b9d16775c4503e4deb48f8c54cf50?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "妮可·罗宾",
            "草帽一伙的考古学家，绰号恶魔之子",
            "“草帽一伙”的考古学家，绰号“恶魔之子”。西海出身，悬赏金1亿3000万贝里。食用了花花果实的能力者。“草帽一伙”第六位加入的成员。梦想“找到空白的100年历史”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/f603918fa0ec08fad52d80525fee3d6d55fbda12?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/d6ca7bcb0a46f21fbe092afd1c767c600c3387440058?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "弗兰奇",
            "草帽一伙的船匠，绰号铁人·弗兰奇",
            "“草帽一伙”的船匠，绰号“铁人·弗兰奇”。南海出身，悬赏金9400万贝里。使用改造后的身体以及自制兵器进行战斗。“草帽一伙”第七位加入的成员。梦想“乘坐自己制作的梦想之船绕伟大航路一周”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/f703738da9773912147fe20cf7198618367ae206?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/8c1001e93901213fb80e0172beb521d12f2eb9383f58?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "布鲁克",
            "草帽一伙的音乐家，绰号鼻呗·布鲁克、灵魂之王",
            "“草帽一伙”的音乐家，绰号“鼻呗·布鲁克”、“灵魂之王”。西海出身，悬赏金8300万贝里。食用了黄泉果实的能力者。使用一把西洋剑战斗的剑士，战斗时会使用黄泉果实的能力作为辅助。“草帽一伙”第八位加入的成员。梦想“与拉布汇合，实现与拉布的约定”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/91529822720e0cf303b510ad0b46f21fbe09aa22?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/962bd40735fae6cd7b897ecfe5e1182442a7d933a158?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "甚平",
            "草帽一伙的舵手，绰号海侠·甚平",
            "“草帽一伙”的舵手，绰号“海侠·甚平”。龙宫王国出身，悬赏金4亿3800万贝里。鱼人族的鲸鲨鱼人，使用鱼人空手道和鱼人柔道进行战斗。“草帽一伙”第九位加入的成员，原王下七武海之一。梦想“帮助路飞成为海贼王，见证鱼人族和人鱼族获得真正的自由”，以此为目标在大海上航行。",
            "https://bkimg.cdn.bcebos.com/pic/03087bf40ad162d987c0fabd12dfa9ec8a13cd61?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5/format,f_auto"
        ),
        VideoBean(
            "https://bkimg.cdn.bcebos.com/pic/e850352ac65c10385343a56d58438413b07eca80235b?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto",
            "前进·梅利号",
            "草帽一伙的第一艘正式的海贼船",
            "制作人：梅利由东海西布罗村的管家梅利设计的梅利号，是草帽一伙的第一艘正式的海贼船。从东海的西布罗村一直到伟大航路的司法之岛，陪伴了草帽一伙很长的一段冒险。由于草帽一伙对梅利的深厚感情，使得梅利号诞生了船灵，在七水之都曾经拖着破损的船身前往司法之岛救下草帽一伙，最终不堪重负，船体崩坏，草帽一伙对其进行了海葬，与梅利号挥泪告别。",
            "https://bkimg.cdn.bcebos.com/pic/32fa828ba61ea8d3fd1fe5266247274e251f95caf2c9?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2U5Mg==,g_7,xp_5,yp_5/format,f_auto"
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
                    val currentTime = TimeUtil.getCurrentTime("yyyy-MM-dd")
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
                        video.introduce = infoBean.introduce
                        video.date = currentTime
                        video.profile = infoBean.profile
                        video.profileDrawing = infoBean.profileDrawing
                        mRandom.nextInt(3000000).also {
                            video.setViews(it)
                        }
                        mRandom.nextInt(1000000).also {
                            video.setFans(it)
                        }
                        mRandom.nextInt(300000).also {
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
            .delay(3000, TimeUnit.MILLISECONDS)
            .compose(RxCompose.applySchedulers())
    }
}