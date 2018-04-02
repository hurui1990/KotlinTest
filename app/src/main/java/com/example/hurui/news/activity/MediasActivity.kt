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

    private var mFragmentList : ArrayList<Fragment>? = null
    private var tabTitleList : ArrayList<String>? = null
    private var medioViewPagerAdapter : MediaViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        activity_pic_toolbar.title = "多媒体文件"
        activity_pic_toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(activity_pic_toolbar)

        tabTitleList = ArrayList()
        tabTitleList!!.add("图片")
        tabTitleList!!.add("视频")
        tabTitleList!!.add("音乐")
        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList!![0]))
        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList!![1]))
        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList!![2]))

        initFragment()
    }

    private fun initFragment(){
        mFragmentList = ArrayList()

        //初始化图片
        val picFragment: Fragment = MediaFragment()
        val pictureBundle = Bundle()
        pictureBundle.putInt("type", 0)
        picFragment.arguments = pictureBundle
        mFragmentList!!.add(picFragment)

        //初始化视频
        val videoFragment: Fragment = MediaFragment()
        val videoBundle = Bundle()
        videoBundle.putInt("type", 1)
        videoFragment.arguments = videoBundle
        mFragmentList!!.add(videoFragment)

        //初始化音乐
        val musicFragment: Fragment = MediaFragment()
        val musicBundle = Bundle()
        musicBundle.putInt("type", 2)
        musicFragment.arguments = musicBundle
        mFragmentList!!.add(musicFragment)

        medioViewPagerAdapter = MediaViewPagerAdapter(supportFragmentManager, mFragmentList!!, tabTitleList!!)
        activity_pic_viewpager.adapter = medioViewPagerAdapter
        activity_pic_tabs.setupWithViewPager(activity_pic_viewpager)
        activity_pic_tabs.setTabsFromPagerAdapter(medioViewPagerAdapter)
    }
}