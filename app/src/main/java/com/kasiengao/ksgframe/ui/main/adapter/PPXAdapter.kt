package com.kasiengao.ksgframe.ui.main.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kaisengao.base.util.GlideUtil
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.ui.main.bean.VideoBean

/**
 * @ClassName: PPXAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/27 21:28
 * @Description: 皮皮虾
 */
class PPXAdapter : BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.item_ppx) {

    override fun convert(holder: BaseViewHolder, item: VideoBean) {
        // 头像
        GlideUtil.loadImage(context, item.avatar, holder.getView<ImageView>(R.id.item_ppx_avatar))
        // 昵称 + 粉丝
        holder.setText(R.id.item_ppx_nickname, "@" + item.nickname)
        // 简介
        holder.setText(R.id.item_ppx_introduce, item.movieName)
        // 赞
        holder.setText(R.id.item_ppx_praise, item.praise)
        // 评论
        holder.setText(R.id.item_ppx_comment, item.comment)
        // 收藏
        holder.setText(R.id.item_ppx_collect, item.share)
    }
}