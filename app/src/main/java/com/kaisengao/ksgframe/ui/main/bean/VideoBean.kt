package com.kaisengao.ksgframe.ui.main.bean

import com.google.gson.annotations.SerializedName
import com.kaisengao.base.util.NumberUtil
import com.kaisengao.base.util.TextUtil
import com.kaisengao.ksgframe.R

/**
 * @ClassName: VideoBean
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/26 17:22
 * @Description:
 */
data class VideoBean(
    var avatar: String,
    var nickname: String,
    var introduce: String,
    var profile: String,
    var profileDrawing: String
) {
    @SerializedName("movieName")
    val movieName: String? = null

    @SerializedName("heightUrl")
    val videoUrl: String? = null

    @SerializedName("coverImg")

    val coverImg: String? = null
    var date: String? = null

    private var mViews = 0
    private var mFans = 0
    private var mPraise = 0
    private var mComment = 0
    private var mShare = 0

    var isPraised = false
    var isStepped = false

    fun setViews(views: Int) {
        mViews = views
    }

    val views: String
        get() = if (mViews <= 0) {
            ""
        } else NumberUtil.formatBigNum(mViews) + TextUtil.getString(R.string.views)

    fun setFans(fans: Int) {
        mFans = fans
    }

    val fans: String
        get() = if (mFans <= 0) {
            ""
        } else NumberUtil.formatBigNum(mFans) + TextUtil.getString(R.string.fans)

    fun setPraise(praise: Int) {
        mPraise = praise
    }

    val praise: String
        get() = if (mPraise <= 0) {
            TextUtil.getString(R.string.interact_praise)
        } else NumberUtil.formatBigNum(mPraise)

    fun addPraise() {
        mPraise += 1
    }

    fun subPraise() {
        mPraise -= 1
    }

    fun setComment(comment: Int) {
        mComment = comment
    }

    val comment: String
        get() = if (mComment <= 0) {
            TextUtil.getString(R.string.interact_comment)
        } else NumberUtil.formatBigNum(mComment)

    fun setShare(share: Int) {
        mShare = share
    }

    val share: String
        get() = if (mShare <= 0) {
            TextUtil.getString(R.string.interact_share)
        } else NumberUtil.formatBigNum(mShare)

    val collect: String
        get() = if (mShare <= 0) {
            TextUtil.getString(R.string.interact_collect)
        } else NumberUtil.formatBigNum(mShare)

    val synopsis: String
        get() = "    拥有财富、名声、权力，这世界上的一切的男人 “海贼王”哥尔·D·罗杰，在被行刑受死之前说了一句话，让全世界的人都涌向了大海。“想要我的宝藏吗？如果想要的话，那就到海上去找吧，我全部都放在那里。”，世界开始迎接“大海贼时代”的来临。\n" +
                "    时值“大海贼时代”，为了寻找传说中海贼王罗杰所留下的大秘宝“ONE PIECE”，无数海贼扬起旗帜，互相争斗。有一个梦想成为海贼王的少年叫路飞，他因误食“恶魔果实”而成为了橡皮人，在获得超人能力的同时付出了一辈子无法游泳的代价。十年后，路飞为实现与因救他而断臂的香克斯的约定而出海，他在旅途中不断寻找志同道合的伙伴，开始了以成为海贼王为目标的冒险旅程。"
}