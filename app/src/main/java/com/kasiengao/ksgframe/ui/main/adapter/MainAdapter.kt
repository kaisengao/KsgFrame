package com.kasiengao.ksgframe.ui.main.adapter

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kaisengao.mvvm.base.fragment.BaseFragment

/**
 * @ClassName: MainAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 21:59
 * @Description:
 */
class MainAdapter(
    fm: FragmentManager,
    private val mFragments: List<BaseFragment<*>>
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = mFragments.size

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getPageTitle(position: Int): CharSequence = mFragments[position].title
}