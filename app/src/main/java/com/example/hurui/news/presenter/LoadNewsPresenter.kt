package com.example.hurui.news.presenter

import com.example.hurui.news.bean.WeatherData
import com.example.hurui.news.model.OnLoadNewsListener
import com.example.hurui.news.model.imlp.LoadNewsModelImpl
import com.example.hurui.news.view.LoadNewsView

/**
 * Created by hurui on 2017/5/25.
 */
class LoadNewsPresenter(private val mLoadNewsView: LoadNewsView) : OnLoadNewsListener{

    private var mLoadNewsModel : LoadNewsModelImpl = LoadNewsModelImpl(this)

    fun loadWeather(city:String){
        mLoadNewsModel.loadWeather(city)
    }

    fun loadNews(type:String) {
        mLoadNewsModel.loadNews(type)
    }

    override fun onLoadSuccess(result: String) {
        mLoadNewsView.setLoadNews(result)
    }

    override fun onLoadFailed(errorType: Int) {
        mLoadNewsView.loadNewsError(errorType)
    }

    override fun onLoadWeatherSuccess(retult: WeatherData) {
        mLoadNewsView.loadWeather(retult)
    }

    override fun onLoadWeatherFailed(errorType: Int) {

    }

}