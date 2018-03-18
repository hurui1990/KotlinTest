package com.example.hurui.news.model.imlp

import android.util.Log
import com.example.hurui.news.model.LoadMediaModel
import com.example.hurui.news.model.OnLoadMediaListener

/**
 * Created by hurui on 2018/3/18.
 */
class LoadMediaModelImlp(private var onLoadMediaListener: OnLoadMediaListener) : LoadMediaModel{

    var TAG = "LoadMediaModelImlp"
    var mOnLoadMediaListener : OnLoadMediaListener = onLoadMediaListener

    override fun loadMedia(type: Int) {
        //TODO 加载图片或视频
        Log.i(TAG, type.toString())
        when (type){
        0 -> {
                getAllPictureInfo()
            }
            1 -> {
                getAllVedioInfo()
            }
        }
    }

    fun getAllPictureInfo(){
        //TODO 加载图片
        Log.i(TAG, "获取所有图片信息")
    }

    fun getAllVedioInfo(){
        //TODO 加载视频
        Log.i(TAG, "获取所有视频信息")
    }

}