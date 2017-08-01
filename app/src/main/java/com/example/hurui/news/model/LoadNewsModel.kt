package com.example.hurui.news.model

/**
 * Created by hurui on 2017/5/25.
 */
interface LoadNewsModel {
    fun loadNews(type:String)
    fun loadWeather(city:String)
}