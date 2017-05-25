package com.example.hurui.news.model

import com.example.hurui.news.bean.NewsDetail

/**
 * Created by hurui on 2017/5/25.
 */
interface OnLoadNewsListener {
    fun onLoadSuccess(result:ArrayList<NewsDetail>)
    fun onLoadFailed(errorType:Int)
}