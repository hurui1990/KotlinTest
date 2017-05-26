package com.example.hurui.news.inter

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


/**
 * Created by hurui on 2017/5/26.
 */
interface LoadNewsService {
    @GET("/toutiao/index")
    fun getNewsResult(@Header("Authorization") appcode: String, @Query("type") type: String): Call<okhttp3.ResponseBody>
}