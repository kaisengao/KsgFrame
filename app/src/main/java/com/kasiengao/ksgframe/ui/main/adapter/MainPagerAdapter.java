package com.kasiengao.ksgframe.ui.main.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.kasiengao.ksgframe.ui.main.bean.MainPagerBean;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: MainPagerAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 10:23
 * @Description:
 */
public class MainPagerAdapter extends PagerAdapter {

    private final List<MainPagerBean> mPagers;

    public MainPagerAdapter() {
        this.mPagers = new ArrayList<>();
    }

    public void addPager(String title, @LayoutRes int layoutRes) {
        this.mPagers.add(new MainPagerBean(title, layoutRes));
        this.notifyDataSetChanged();
    }

    public void setPagers(List<MainPagerBean> pagers) {
        this.mPagers.clear();
        this.mPagers.addAll(pagers);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPagers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mPagers.get(position).getTitle();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = View.inflate(container.getContext(), mPagers.get(position).getLayoutRes(), null);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }
}