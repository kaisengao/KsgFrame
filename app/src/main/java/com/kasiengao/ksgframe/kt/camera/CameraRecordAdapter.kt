package com.kasiengao.ksgframe.kt.camera

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kaisengao.uvccamera.utils.FileUtil
import com.kasiengao.ksgframe.R

/**
 * @ClassName: CameraRecordAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/13 13:54
 * @Description:
 */
class CameraRecordAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_camera_record) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.record_name, FileUtil.parseName(item))
        holder.setImageBitmap(R.id.record_cover, FileUtil.getFileVideoCover(item))
    }
}