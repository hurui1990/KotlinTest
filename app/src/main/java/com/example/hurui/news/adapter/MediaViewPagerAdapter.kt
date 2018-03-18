package com.example.hurui.news.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by hurui on 2018/3/18.
 */
class MediaViewPagerAdapter(fragmentManager: FragmentManager,
                            private val mFragments: ArrayList<Fragment>, private val mTitleList : ArrayList<String>) : FragmentStatePagerAdapter(fragmentManager){
    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitleList[position]
    }
}