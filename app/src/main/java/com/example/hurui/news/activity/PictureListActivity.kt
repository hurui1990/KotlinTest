package com.example.hurui.news.activity

import com.example.hurui.news.R
import com.example.hurui.news.base.BaseActivity
import com.example.hurui.news.bean.MediaBean
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_media_detail.*

class PictureListActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_media_detail
    }

    override fun initView() {
        super.initView()

        toolbar.apply {
            title = intent.getStringExtra("title")!!.split("/").last()
            setTitleTextColor(resources.getColor(R.color.white))
            setSupportActionBar(this)
        }

        val data = intent.getStringExtra("list")
        var list = Gson().fromJson(data,Array<MediaBean>::class.java).toMutableList()


    }
}