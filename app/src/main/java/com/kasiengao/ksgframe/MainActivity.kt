package com.kasiengao.ksgframe

import android.content.Intent
import androidx.appcompat.widget.AppCompatImageView
import com.kasiengao.ksgframe.java.JavaActivity
import com.kasiengao.ksgframe.kt.KtActivity
import com.kasiengao.mvp.java.BaseToolbarActivity

/**
 * @ClassName: MainActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 15:36
 * @Description: 启动页面
 */
class MainActivity : BaseToolbarActivity() {

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun initWidget() {
        super.initWidget()
        // Toolbar Back
        this.setNavigationIcon(R.drawable.icon_main_smiling_face)
        // Kt 语言版本
        this.findViewById<AppCompatImageView>(R.id.main_kt).setOnClickListener {
            startActivity(Intent(this, KtActivity::class.java))
        }
        // Java 语言版本
        this.findViewById<AppCompatImageView>(R.id.main_java).setOnClickListener {
            startActivity(Intent(this, JavaActivity::class.java))
        }
    }
}
