package com.example.hurui.news.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.hurui.news.bean.NewType

/**
 * Created by hurui on 2017/8/4.
 */

class ViewpagerAdapter(fragmentManager: FragmentManager,
                       private val mFragments: ArrayList<Fragment>,
                       private val mTabs: ArrayList<NewType>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTabs[position].name
    }
}
