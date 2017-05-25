package com.example.hurui.news.model.imlp

import com.example.hurui.news.model.LoadNewsModel
import com.example.hurui.news.model.OnLoadNewsListener
import com.example.hurui.news.utils.Utils

/**
 * Created by hurui on 2017/5/25.
 */
class LoadNewsModelImlp(onLoadNewsListener: OnLoadNewsListener) : LoadNewsModel{

    var mOnLoadNewsListener : OnLoadNewsListener? = null

    init {
        mOnLoadNewsListener = onLoadNewsListener
    }

    override public fun loadNews(type: String) {
        mOnLoadNewsListener!!.onLoadFailed(Utils.NO_INERNET_CONNECT)
    }
}