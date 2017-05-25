package com.example.hurui.news.view

import com.example.hurui.news.bean.NewsDetail

/**
 * Created by hurui on 2017/5/25.
 */
interface LoadNewsView {
    fun setLoadNews(result:ArrayList<NewsDetail>)
    fun loadNewsError(errorType:Int)
}