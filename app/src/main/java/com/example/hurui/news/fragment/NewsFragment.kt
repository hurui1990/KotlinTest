package com.example.hurui.news.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.news.R
import com.example.hurui.news.activity.NewsDetailActivity
import com.example.hurui.news.adapter.RecyclerAdapter
import com.example.hurui.news.bean.NewsDetail
import com.example.hurui.news.bean.Result
import com.example.hurui.news.bean.WeatherData
import com.example.hurui.news.presenter.LoadNewsPresenter
import com.example.hurui.news.view.LoadNewsView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_news.*
import java.util.*

/**
 * Created by hurui on 2017/8/4.
 */

class NewsFragment : Fragment(), LoadNewsView, RecyclerAdapter.OnItemClickListener {
    private val TAG = "NewsFragment"
    var mLoadNewsPresenter : LoadNewsPresenter? = null

    var dataAdapter : RecyclerAdapter? = null
    var dataList : ArrayList<NewsDetail>? =null
    var mType : String? = null
    var share : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle : Bundle = arguments
        mType = bundle.getString("type")

        mLoadNewsPresenter = LoadNewsPresenter(this)
        dataList = ArrayList<NewsDetail>()
        dataAdapter = RecyclerAdapter(activity)
        dataAdapter!!.setHasStableIds(true)
        dataAdapter!!.setData(dataList!!)

        dataAdapter!!.setItemClickListener(this)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater!!.inflate(R.layout.fragment_news, container, false)
        return view
    }

    override fun onItemClick(view: View, position: Int) {
        var itemData = dataList!![position]
        var intent : Intent = Intent(activity, NewsDetailActivity::class.java)
        intent.putExtra("url", itemData.url)
        intent.putExtra("title", itemData.title)
        activity.startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        recycler_content.layoutManager = LinearLayoutManager(activity)
        recycler_content.adapter = dataAdapter
        mLoadNewsPresenter!!.loadNews(mType!!)
    }

    override fun setLoadNews(result: String) {
        var gson : Gson = Gson()
        var result_gson : Result = gson.fromJson(result, Result::class.java)
        dataList!!.clear()
        dataList = result_gson.result.data
        dataAdapter?.setData(dataList!!)

        share  = activity.getSharedPreferences("result",Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = share!!.edit()
        editor.putString("hurui", result)
        editor.commit()

    }

    override fun loadNewsError(errorType: Int) {
        Log.i(TAG, "failed : " + errorType)
        if(errorType == 400){
            share = activity.getSharedPreferences("result", Context.MODE_PRIVATE)
            val result : String = share!!.getString("hurui", "")
            if(result != null && !TextUtils.isEmpty(result)){
                var gson : Gson = Gson()
                var result_gson : Result = gson.fromJson(result, Result::class.java)
                dataList!!.clear()
                dataList = result_gson.result.data
                dataAdapter?.setData(dataList!!)
            }
        }
    }

    override fun loadWeather(result: WeatherData) {}

    override fun loadWeatherError(errorType: Int) {}
}
