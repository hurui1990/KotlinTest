package com.example.hurui.news.model

import com.example.hurui.news.bean.WeatherData

/**
 * Created by hurui on 2017/5/25.
 */
interface OnLoadNewsListener {
    fun onLoadSuccess(result:String)
    fun onLoadFailed(errorType:Int)
    fun onLoadWeatherSuccess(result:WeatherData)
    fun onLoadWeatherFailed(errorType:Int)
}