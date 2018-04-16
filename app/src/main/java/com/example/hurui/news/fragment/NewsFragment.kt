package com.example.hurui.news.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
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
import com.example.hurui.news.view.MyDivider
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_news.*
import java.util.*

/**
 * Created by hurui on 2017/8/4.
 */

class NewsFragment : Fragment(), LoadNewsView, RecyclerAdapter.OnItemClickListener {
    private var mLoadNewsPresenter : LoadNewsPresenter? = null

    private var dataAdapter : RecyclerAdapter? = null
    private var dataList : ArrayList<NewsDetail>? =null
    private var mType : String? = null
    private var share : SharedPreferences? = null
    private var needload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle : Bundle = arguments
        mType = bundle.getString("type")

        mLoadNewsPresenter = LoadNewsPresenter(this)
        dataList = ArrayList()
        dataAdapter = RecyclerAdapter(activity)
        dataAdapter!!.setHasStableIds(true)
        dataAdapter!!.setData(dataList!!)

        dataAdapter!!.setItemClickListener(this)
        needload = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_news, container, false)
    }

    override fun onItemClick(view: View, position: Int) {
        val itemData = dataList!![position]
        val intent = Intent(activity, NewsDetailActivity::class.java)
        intent.putExtra("url", itemData.url)
        intent.putExtra("title", itemData.title)
        activity.startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        if(needload) {
            recycler_content.layoutManager = LinearLayoutManager(activity)
            recycler_content.addItemDecoration(MyDivider(activity, DividerItemDecoration.VERTICAL))
            recycler_content.adapter = dataAdapter
            mLoadNewsPresenter!!.loadNews(mType!!)
            needload = false
        }
    }

    override fun setLoadNews(result: String) {
        val gson = Gson()
        val resultGson : Result = gson.fromJson(result, Result::class.java)
        dataList!!.clear()
        dataList = resultGson.result.data
        dataAdapter?.setData(dataList!!)

        share  = activity.getSharedPreferences("result",Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = share!!.edit()
        editor.putString("hurui", result)
        editor.commit()

    }

    override fun loadNewsError(errorType: Int) {
        if(errorType == 400 && activity != null){
            share = activity.getSharedPreferences("result", Context.MODE_PRIVATE)
            val result : String = share!!.getString("hurui", "")
            if(result != null && !TextUtils.isEmpty(result)){
                val gson = Gson()
                val resultGson : Result = gson.fromJson(result, Result::class.java)
                dataList!!.clear()
                dataList = resultGson.result.data
                dataAdapter?.setData(dataList!!)
            }
        }
    }

    override fun loadWeather(result: WeatherData) {}

    override fun loadWeatherError(errorType: Int) {}
}
