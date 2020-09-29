package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.hurui.news.R
import com.example.hurui.news.adapter.PhotoViewAdapter
import com.example.hurui.news.base.BaseActivity
import kotlinx.android.synthetic.main.activity_photoview.*

/**
 * Created by hurui on 2018/3/19.
 */
class PhotoViewActivity : BaseActivity(){

    override fun getLayoutId(): Int {
        return R.layout.activity_photoview
    }

    override fun initView() {
        super.initView()
        val position : Int = intent.getIntExtra("position",-1)
        val list : ArrayList<String> = intent.getStringArrayListExtra("list") as ArrayList<String>

        photo_viewpager.adapter = PhotoViewAdapter(list, this)
        Log.i("===========", position.toString())
        photo_viewpager.setCurrentItem(position, false)
    }
}