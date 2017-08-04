package com.example.hurui.news.model.imlp

import android.util.Log
import com.example.hurui.news.bean.Result
import com.example.hurui.news.bean.WeatherData
import com.example.hurui.news.model.LoadNewsModel
import com.example.hurui.news.model.OnLoadNewsListener
import com.example.hurui.news.network.LoadNewsService
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by hurui on 2017/5/25.
 */
class LoadNewsModelImlp(internal var onLoadNewsListener: OnLoadNewsListener) : LoadNewsModel {

    internal var newsBaseUrl = "http://v.juhe.cn"
    internal var weatherBaseUrl = "https://free-api.heweather.com"
    internal var newsCode = "53bd93e93a2b5c03c61983294614c91f"
    internal var weatherCode = "84025e047503493c973343f0c619bd22"
    var mOnLoadNewsListener : OnLoadNewsListener = onLoadNewsListener
    var content : String = "{'reason': '成功的返回','result': {'stat': '1','data': [{'uniquekey': 'd00ab6bc1d76967beefb9980dbec25da','title': '坚守38年，只为山里娃的读书梦','date': '2017-05-26 21:01','category': '头条','author_name': '新华社','url': 'http://mini.eastday.com/mobile/170526210105910.html','thumbnail_pic_s': 'http://09.imgmini.eastday.com/mobile/20170526/20170526210105_f98111c52883cbae6eddcab9e2ec905b_3_mwpm_03200403.jpeg','thumbnail_pic_s02': 'http://09.imgmini.eastday.com/mobile/20170526/20170526210105_6b625adf37eac05135e320d860ac7f61_5_mwpm_03200403.jpeg','thumbnail_pic_s03': 'http://09.imgmini.eastday.com/mobile/20170526/20170526210105_0cf02645ffa3604baa71bde33049a9c1_7_mwpm_03200403.jpeg' },{'title': '我们毕业了！——中国唯一一所艾滋病患儿学校迎来毕业季','date': '2017-05-26 20:47','category': '头条','author_name': '新华社','url': 'http://mini.eastday.com/mobile/170526204731243.html','thumbnail_pic_s': 'http://04.imgmini.eastday.com/mobile/20170526/20170526204731_887042f1cc1868714765360b57ebb09e_6_mwpm_03200403.jpeg','thumbnail_pic_s02': 'http://04.imgmini.eastday.com/mobile/20170526/20170526204731_0220e9c4ef2337dc1a24f10c7fec4d4b_1_mwpm_03200403.jpeg','thumbnail_pic_s03': 'http://04.imgmini.eastday.com/mobile/20170526/20170526204731_85df4ef1733fd7d87f929f90b47ca3a0_4_mwpm_03200403.jpeg' },{'uniquekey': '030eed5f2f0b34ec09fd619471152501','title': '面向大海 拥抱“金砖”——写在金砖国家领导人厦门会晤倒计时100天之际','date': '2017-05-26 20:38','category': '头条','author_name': '新华社','url': 'http://mini.eastday.com/mobile/170526203859978.html','thumbnail_pic_s': 'http://08.imgmini.eastday.com/mobile/20170526/20170526203859_745c538727a414e79753deb6acdd4655_3_mwpm_03200403.jpeg','thumbnail_pic_s02': 'http://08.imgmini.eastday.com/mobile/20170526/20170526203859_ba13b8deddeade80ade7c9f635f9a1c5_1_mwpm_03200403.jpeg','thumbnail_pic_s03': 'http://08.imgmini.eastday.com/mobile/20170526/20170526203859_6d40fc4b6db61e2d2b6a793934995ced_7_mwpm_03200403.jpeg' },] },'error_code': 0 }"

    override fun loadNews(type: String) {

        val retrofit = Retrofit.Builder()
                .baseUrl(newsBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val loadNewsService = retrofit.create(LoadNewsService::class.java)
        val callback = loadNewsService.getNewsResult (type,newsCode)

        callback.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                var jsonStr : String = String(response!!.body()!!.bytes())
                var gson : Gson = Gson()
                var result : Result = gson.fromJson(jsonStr,Result::class.java)
                mOnLoadNewsListener.onLoadSuccess(result.result.data)
            }

            override fun onFailure(call: Call<okhttp3.ResponseBody>?, t: Throwable?) {
                Log.i("==============fa", t.toString())
            }
        })

//        var gson : Gson = Gson()
//        var result : Result = gson.fromJson(content,Result::class.java)
//        mOnLoadNewsListener.onLoadSuccess(result.result.data)
    }

    override fun loadWeather(city: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(weatherBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val loadNewsService = retrofit.create(LoadNewsService::class.java)
        val callback = loadNewsService.getWeather(city, weatherCode)

        callback.enqueue(object : Callback<WeatherData>{
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.i("===============", "错误信息"+t!!.message)
            }

            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                mOnLoadNewsListener.onLoadWeatherSuccess(response!!.body()!!)
            }
        })

    }
}