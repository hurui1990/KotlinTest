package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.hurui.news.R
import com.example.hurui.news.adapter.MediaViewPagerAdapter
import com.example.hurui.news.fragment.MediaFragment
import kotlinx.android.synthetic.main.activity_picture.*

/**
 * Created by hurui on 2018/3/18.
 */
class MediasActivity : AppCompatActivity(){

    var mFragmentList : ArrayList<Fragment>? = null
    var tabTitleList : ArrayList<String>? = null
    var medioViewPagerAdapter : MediaViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        activity_pic_toolbar.title = "图片和视频"
        activity_pic_toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(activity_pic_toolbar)

        tabTitleList = ArrayList<String>()
        tabTitleList!!.add("图片")
        tabTitleList!!.add("视频")
        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList!![0]))
        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList!![1]))

        initFragment()
    }

    fun initFragment(){
        mFragmentList = ArrayList<Fragment>()

        //初始化图片
        var picFragment: Fragment = MediaFragment()
        var pictureBundle = Bundle()
        pictureBundle.putInt("type", 0)
        picFragment.arguments = pictureBundle
        mFragmentList!!.add(picFragment)

        //初始化视频
        var videoFragment: Fragment = MediaFragment()
        var videoBundle = Bundle()
        videoBundle.putInt("type", 1)
        videoFragment.arguments = videoBundle
        mFragmentList!!.add(videoFragment)

        medioViewPagerAdapter = MediaViewPagerAdapter(supportFragmentManager, mFragmentList!!, tabTitleList!!)
        activity_pic_viewpager.adapter = medioViewPagerAdapter
        activity_pic_tabs.setupWithViewPager(activity_pic_viewpager)
        activity_pic_tabs.setTabsFromPagerAdapter(medioViewPagerAdapter)
    }
}