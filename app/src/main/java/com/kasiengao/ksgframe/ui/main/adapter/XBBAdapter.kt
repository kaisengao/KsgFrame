package com.kasiengao.ksgframe.ui.main.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.button.MaterialButton
import com.kaisengao.base.util.GlideUtil
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.ui.main.bean.VideoBean

/**
 * @ClassName: XBBAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/5 17:09
 * @Description: 象拔蚌
 */
class XBBAdapter : BaseQuickAdapter<VideoBean, XBBAdapter.ViewHolder>(R.layout.item_xbb) {

    override fun convert(holder: ViewHolder, item: VideoBean) {
        // 头像
        GlideUtil.loadImage(context, item.avatar, holder.getView<ImageView>(R.id.item_video_avatar))
        // 昵称
        holder.setText(R.id.item_video_nickname, item.nickname)
        // 简介
        holder.setText(R.id.item_video_introduce, item.introduce)
        // 标题
        holder.setText(R.id.item_video_title, item.movieName)
        // 赞
        holder.mPraiseView.text = item.praise
        holder.mPraiseView.isSelected = item.isPraised
        // 踩
        holder.getView<View>(R.id.item_video_interact_stepped).isSelected = item.isStepped
        // 评论
        holder.setText(R.id.item_video_interact_comment, item.comment)
        // 分享
        holder.setText(R.id.item_video_interact_share, item.share)
        // 设置封面图
        holder.mPlayContainer.setCoverImage(item.coverImg)
    }

    inner class ViewHolder(view: View) : BaseViewHolder(view) {
        val mPraiseView: MaterialButton by lazy {
            this.getView(R.id.item_video_interact_praise)
        }

        private val mSteppedView: MaterialButton by lazy {
            this.getView(R.id.item_video_interact_stepped)
        }

        val mPlayContainer: PlayerContainerView by lazy {
            this.getView(R.id.item_player_container)
        }

        init {
            // 视频容器 设置比例16/9
            val layoutParams = mPlayContainer.layoutParams
            layoutParams.height = recyclerView.width * 9 / 16
            this.mPlayContainer.requestLayout()
            // 初始状态位
//            this.mPlayContainer.setPlayerState(IPlayer.STATE_IDLE);
            // 赞
            this.getView<View>(R.id.item_video_interact_praise).setOnClickListener { v: View ->
                val videoBean: VideoBean = data[layoutPosition]
                videoBean.isPraised = !videoBean.isPraised
                if (videoBean.isPraised) {
                    videoBean.addPraise()
                } else {
                    videoBean.subPraise()
                }
                this.mPraiseView.text = videoBean.praise
                this.mPraiseView.isSelected = videoBean.isPraised
            }
            // 踩
            this.getView<View>(R.id.item_video_interact_stepped).setOnClickListener { v: View ->
                val videoBean: VideoBean = data[layoutPosition]
                videoBean.isStepped = !videoBean.isStepped
                this.mSteppedView.isSelected = videoBean.isStepped
            }
            // 评论
            this.getView<View>(R.id.item_video_interact_comment).setOnClickListener { v: View ->
                val layoutPosition = layoutPosition
                // 进入详情页
//                ListPlayer.getInstance()
//                    .onEnterDetail(layoutPosition, data[layoutPosition], mPlayContainer)
            }
            // 分享
            this.getView<View>(R.id.item_video_interact_share).setOnClickListener { v: View ->
                // 回调上级
                setOnItemChildClick(v, layoutPosition)
            }
            // Play
            this.mPlayContainer.setOnClickListener { v: View ->
                val layoutPosition = layoutPosition
                // 回调上级
                setOnItemChildClick(v, layoutPosition)
            }
            // ItemClick
            view.setOnClickListener { v: View ->
                val layoutPosition = layoutPosition
                // 进入详情页
//                ListPlayer.getInstance().onEnterDetail(layoutPosition, getData().get(layoutPosition), mPlayContainer);
            }
        }
    }
}