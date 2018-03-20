package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.hurui.news.R
import com.example.hurui.news.adapter.PhotoViewAdapter
import kotlinx.android.synthetic.main.activity_photoview.*

/**
 * Created by hurui on 2018/3/19.
 */
class PhotoViewActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoview)

        val position : Int = intent.getIntExtra("position",-1)
        val list : ArrayList<String> = intent.getStringArrayListExtra("list")

        photo_viewpager.adapter = PhotoViewAdapter(list, this)
        Log.i("===========", position.toString())
        photo_viewpager.setCurrentItem(position, false)
    }
}