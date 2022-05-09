package com.kasiengao.ksgframe.ui.main.bean

import com.google.gson.annotations.SerializedName
import com.kaisengao.base.util.NumberUtil
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil

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
}