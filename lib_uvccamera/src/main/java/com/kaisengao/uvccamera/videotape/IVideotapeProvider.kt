package com.kaisengao.uvccamera.videotape

/**
 * @ClassName: IVideotapeProvider
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/2 10:38
 * @Description:
 */
interface IVideotapeProvider<Bitmap> {

    /**
     * bitmap list size, you can set like
     *
     * return bitmapList.size()
     */
    fun size(): Int

    /**
     * the next bitmap
     */
    operator fun next(): Bitmap

    /**
     * progress
     * If 1f is returned, progress is complete
     * A return of -1 indicates failure
     */
    fun progress(progress: Float)

}