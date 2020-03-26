package com.kasiengao.ksgframe.kt

import com.kasiengao.ksgframe.R
import com.kasiengao.mvp.kt.BaseToolbarActivity

/**
 * @ClassName: KtActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 18:44
 * @Description: Kotlin 版本
 */
class KtActivity : BaseToolbarActivity() {

    override fun getContentLayoutId(): Int = R.layout.activity_kt

    override fun initWidget() {
        super.initWidget()
        // Toolbar Title
        this.setTitle(R.string.kotlin_title)
    }

}