package com.example.hurui.news.model.imlp

import android.content.Context
import android.util.Log
import com.example.hurui.news.activity.MediaAsyncTask
import com.example.hurui.news.model.LoadMediaModel
import com.example.hurui.news.model.OnLoadMediaListener


/**
 * Created by hurui on 2018/3/18.
 */
class LoadMediaModelImlp(onLoadMediaListener: OnLoadMediaListener) : LoadMediaModel{

    var TAG = "LoadMediaModelImlp"
    var mOnLoadMediaListener : OnLoadMediaListener = onLoadMediaListener

    override fun loadMedia(type: Int, context : Context) {
        //TODO 加载图片或视频
        Log.i(TAG, type.toString())
        MediaAsyncTask(context, mOnLoadMediaListener, type).execute()
    }

}