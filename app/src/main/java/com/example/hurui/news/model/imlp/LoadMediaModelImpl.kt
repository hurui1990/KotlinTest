package com.example.hurui.news.model.imlp

import android.content.Context
import android.util.Log
import com.example.hurui.news.model.LoadMediaModel
import com.example.hurui.news.model.OnLoadMediaListener
import com.example.hurui.news.network.MediaAsyncTask


/**
 * Created by hurui on 2018/3/18.
 */
class LoadMediaModelImpl(private val mOnLoadMediaListener: OnLoadMediaListener) : LoadMediaModel{

    private var TAG = "LoadMediaModelImlp"

    override fun loadMedia(type: Int, context : Context) {
        //加载图片或视频
        Log.i(TAG, type.toString())
        MediaAsyncTask(context, mOnLoadMediaListener, type).execute()
    }

}