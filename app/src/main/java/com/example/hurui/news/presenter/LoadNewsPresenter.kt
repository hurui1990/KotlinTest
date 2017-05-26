package com.example.hurui.news.presenter

import com.example.hurui.news.bean.NewsDetail
import com.example.hurui.news.model.OnLoadNewsListener
import com.example.hurui.news.model.imlp.LoadNewsModelImlp
import com.example.hurui.news.view.LoadNewsView

/**
 * Created by hurui on 2017/5/25.
 */
class LoadNewsPresenter(loadNewsView: LoadNewsView) : OnLoadNewsListener{

    var mLoadNewsView : LoadNewsView = loadNewsView
    var mLoadNewsModel : LoadNewsModelImlp = LoadNewsModelImlp(this)

    public fun loadNews(type:String) {
        mLoadNewsModel!!.loadNews(type)
    }

    override fun onLoadSuccess(result: ArrayList<NewsDetail>) {
        mLoadNewsView!!.setLoadNews(result)
    }

    override fun onLoadFailed(errorType: Int) {
        mLoadNewsView!!.loadNewsError(errorType)
    }

}