package com.example.hurui.news.presenter

import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.model.OnLoadMediaListener
import com.example.hurui.news.model.imlp.LoadMediaModelImlp
import com.example.hurui.news.view.LoadMediaView

/**
 * Created by hurui on 2018/3/18.
 */
class LoadMediaPresenter(loadMediaView : LoadMediaView) : OnLoadMediaListener{

    var mLoadMediaListener : LoadMediaView = loadMediaView
    var mLoadMediaModel : LoadMediaModelImlp = LoadMediaModelImlp(this)

    fun loadAllMedia(type : Int){
        mLoadMediaModel!!.loadMedia(type)
    }

    override fun onLoadSuccess(result: ArrayList<MediaBean>) {

    }

}