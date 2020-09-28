package com.example.hurui.news.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import com.example.hurui.news.R
import com.example.hurui.news.adapter.MediaViewPagerAdapter
import com.example.hurui.news.base.BaseActivity
import com.example.hurui.news.fragment.MediaFragment
import com.example.hurui.news.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_picture.*

/**
 * Created by hurui on 2018/3/18.
 */
class MediasActivity : BaseActivity(){

    private lateinit var mFragmentList : ArrayList<Fragment>
    private val tabTitleList by lazy { arrayListOf("图片","视频","音乐") }
    private lateinit var mediaViewPagerAdapter : MediaViewPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_picture
    }

    override fun initView() {
        super.initView()
        activity_pic_toolbar.title = "多媒体文件"
        activity_pic_toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(activity_pic_toolbar)

        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList[0]))
        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList[1]))
        activity_pic_tabs.addTab(activity_pic_tabs.newTab().setText(tabTitleList[2]))

        if (PermissionUtil.hasPermission(this,permissions)){
            initFragment()
        }else{
            PermissionUtil.requestPermissions(
                    this,
                    permissions,
                    PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT
            )
        }

    }

    private fun initFragment(){
        mFragmentList = ArrayList()

        //初始化图片
        val picFragment = MediaFragment()
        val pictureBundle = Bundle()
        pictureBundle.putInt("type", 0)
        picFragment.arguments = pictureBundle
        mFragmentList.add(picFragment)

        //初始化视频
        val videoFragment = MediaFragment()
        val videoBundle = Bundle()
        videoBundle.putInt("type", 1)
        videoFragment.arguments = videoBundle
        mFragmentList.add(videoFragment)

        //初始化音乐
        val musicFragment = MediaFragment()
        val musicBundle = Bundle()
        musicBundle.putInt("type", 2)
        musicFragment.arguments = musicBundle
        mFragmentList.add(musicFragment)

        mediaViewPagerAdapter = MediaViewPagerAdapter(supportFragmentManager, mFragmentList, tabTitleList)
        activity_pic_viewpager.adapter = mediaViewPagerAdapter
        activity_pic_tabs.setupWithViewPager(activity_pic_viewpager)
        activity_pic_tabs.setTabsFromPagerAdapter(mediaViewPagerAdapter)
    }

    /**
     * 对权限的处理
     * */
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT === requestCode) {
            for (permission in permissions) {
                if (PermissionChecker.checkSelfPermission(this, permission) != PermissionChecker.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        myToast("请设置相应的权限")
                        return
                    }
                }
            }
            initFragment()
        }
    }
}