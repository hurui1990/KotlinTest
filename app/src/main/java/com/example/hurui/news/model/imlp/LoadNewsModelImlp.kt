package com.example.hurui.news.model.imlp

import android.util.Log

import com.example.hurui.news.bean.NewsDetail
import com.example.hurui.news.bean.Result
import com.example.hurui.news.model.LoadNewsModel
import com.example.hurui.news.model.OnLoadNewsListener
import com.example.hurui.news.inter.LoadNewsService
import com.google.gson.Gson
import org.json.JSONObject

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by hurui on 2017/5/25.
 */
class LoadNewsModelImlp(internal var onLoadNewsListener: OnLoadNewsListener) : LoadNewsModel {

    internal var baseUrl = "http://toutiao-ali.juheapi.com"
    internal var appCode = "5b52ab9c7ccd4e13b5639ca2e3d1d723"
    var mOnLoadNewsListener : OnLoadNewsListener = onLoadNewsListener

    override fun loadNews(type: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val loadNewsService = retrofit.create(LoadNewsService::class.java)
        val callback = loadNewsService.getNewsResult("APPCODE " + appCode, type)

        callback.enqueue(object : Callback<okhttp3.ResponseBody>{
            override fun onResponse(call: Call<okhttp3.ResponseBody>?, response: Response<okhttp3.ResponseBody>?) {
                var jsonStr : String = String(response!!.body()!!.bytes())
                var gson : Gson = Gson()
                var result : Result = gson.fromJson(jsonStr,Result::class.java)
                mOnLoadNewsListener.onLoadSuccess(result.result.data)
            }

            override fun onFailure(call: Call<okhttp3.ResponseBody>?, t: Throwable?) {
                Log.i("==============fa", t.toString())
            }
        })
    }
}