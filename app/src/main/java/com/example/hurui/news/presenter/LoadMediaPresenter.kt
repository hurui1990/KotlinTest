package com.example.hurui.news.presenter

import android.content.Context
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.model.OnLoadMediaListener
import com.example.hurui.news.model.imlp.LoadMediaModelImpl
import com.example.hurui.news.view.LoadMediaView

/**
 * Created by hurui on 2018/3/18.
 */
class LoadMediaPresenter(private val mLoadMediaListener : LoadMediaView) : OnLoadMediaListener{

    private var mLoadMediaModel : LoadMediaModelImpl = LoadMediaModelImpl(this)

    fun loadAllMedia(type : Int, context : Context){
        mLoadMediaModel.loadMedia(type, context)
    }

    override fun onLoadSuccess(result: HashMap<String,ArrayList<MediaBean>>) {
        mLoadMediaListener.loadAllMedia(result)
    }

}