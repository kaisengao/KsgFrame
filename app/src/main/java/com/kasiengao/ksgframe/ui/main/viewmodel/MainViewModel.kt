package com.kasiengao.ksgframe.ui.main.viewmodel

import android.app.Application
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import com.kaisengao.base.util.KLog
import com.kaisengao.mvvm.base.viewmodel.BaseViewModel
import com.kaisengao.mvvm.binding.command.BindingParam
import com.kaisengao.mvvm.binding.command.BindingParamImp

/**
 * @ClassName: MainViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 11:46
 * @Description:
 */
public class MainViewModel(application: Application) : BaseViewModel(application) {

    public val progress1 = BindingParamImp(BindingParam<Int> { progress ->
        KLog.d("zzz", "progress1 = $progress")
    })

    public val progress2 = BindingParamImp(BindingParam<Int> { progress ->
        KLog.d("zzz", "progress2 = $progress")
    })

    companion object {

        @BindingAdapter("setTest")
        @JvmStatic
        fun setTest(porgress: SeekBar, paramImp: BindingParamImp<Int>? ) {
            porgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (paramImp != null) {
                        paramImp.execute(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }
    }
}