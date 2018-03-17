package com.example.hurui.news.view

import com.example.hurui.news.bean.WeatherData

/**
 * Created by hurui on 2017/5/25.
 */
interface LoadNewsView {
    fun setLoadNews(result:String)
    fun loadNewsError(errorType:Int)
    fun loadWeather(result:WeatherData)
    fun loadWeatherError(errorType: Int)
}