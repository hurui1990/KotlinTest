package com.example.hurui.news.network

import com.example.hurui.news.bean.WeatherData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url


/**
 * Created by hurui on 2017/5/26.
 */
interface LoadNewsService {
    @GET("/toutiao/index")
    fun getNewsResult(@Header("Authorization") appcode: String, @Query("type") type: String): Call<ResponseBody>

    @GET("/v5/weather")
    fun getWeather(@Query("city") city : String, @Query("key") key : String) : Call<ResponseBody>
}