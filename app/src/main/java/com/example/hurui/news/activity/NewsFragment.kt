package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.news.R
import com.example.hurui.news.adapter.RecyclerAdapter
import com.example.hurui.news.bean.NewsDetail
import com.example.hurui.news.bean.WeatherData
import com.example.hurui.news.presenter.LoadNewsPresenter
import com.example.hurui.news.view.LoadNewsView
import kotlinx.android.synthetic.main.fragment_news.*
import java.util.ArrayList

/**
 * Created by hurui on 2017/8/4.
 */

class NewsFragment : Fragment(), LoadNewsView {

    var mLoadNewsPresenter : LoadNewsPresenter? = null

    var dataAdapter : RecyclerAdapter? = null
    var dataList : ArrayList<NewsDetail>? =null
    var mType : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle : Bundle = arguments
        mType = bundle.getString("type")

        mLoadNewsPresenter = LoadNewsPresenter(this)
        dataList = ArrayList<NewsDetail>()
        dataAdapter = RecyclerAdapter(activity)
        dataAdapter!!.setHasStableIds(true)
        dataAdapter!!.setData(dataList!!)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater!!.inflate(R.layout.fragment_news, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        recycler_content.layoutManager = LinearLayoutManager(activity)
        recycler_content.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recycler_content.adapter = dataAdapter

        mLoadNewsPresenter!!.loadNews(mType!!)
    }

    override fun setLoadNews(result: ArrayList<NewsDetail>) {
        dataAdapter?.setData(result)
    }

    override fun loadNewsError(errorType: Int) {

    }

    override fun loadWeather(result: WeatherData) {

    }

    override fun loadWeatherError(errorType: Int) {

    }
}
