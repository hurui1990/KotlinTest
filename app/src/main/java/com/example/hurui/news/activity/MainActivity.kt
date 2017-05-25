package com.example.hurui.news.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.example.hurui.news.R
import com.example.hurui.news.bean.NewType
import com.example.hurui.news.bean.NewsDetail
import com.example.hurui.news.presenter.LoadNewsPresenter
import com.example.hurui.news.utils.Utils
import com.example.hurui.news.view.LoadNewsView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() ,LoadNewsView{
    var newTypes : ArrayList<NewType>? = null
    var mLoadNewsPresenter : LoadNewsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLoadNewsPresenter = LoadNewsPresenter(this)

        initScrollView()
    }

    //动态添加头部的新闻类型标签的布局
    fun initScrollView(){
        newTypes = ArrayList<NewType>()
        newTypes!!.add(NewType("头条","top"))
        newTypes!!.add(NewType("社会","shehui"))
        newTypes!!.add(NewType("国内","guonei"))
        newTypes!!.add(NewType("国际","guoji"))
        newTypes!!.add(NewType("娱乐","yule"))
        newTypes!!.add(NewType("体育","tiyu"))
        newTypes!!.add(NewType("军事","junshi"))
        newTypes!!.add(NewType("科技","keji"))
        newTypes!!.add(NewType("财经","caijing"))
        newTypes!!.add(NewType("时尚","shishang"))


        val screenWidth : Int = Utils.getScreenWidth(this)

        for (i in newTypes!!.indices){
            val newType : NewType = newTypes!!.get(i)
            val textView : TextView = TextView(this)
            textView.text = newType.name
            textView.gravity = Gravity.START
            textView.textSize = resources.getDimension(R.dimen.title_text_size)
            textView.width = screenWidth / 7
            textView.setTextColor(resources.getColor(R.color.text_color))
            if (i == 0) {
                textView.setTextColor(resources.getColor(R.color.select_text_color))
                mLoadNewsPresenter!!.loadNews(newType.type)
            }
            textView.setOnClickListener({OnClickNewType(newType)})
            title_view.addView(textView)
        }
    }

    fun OnClickNewType(oldType: NewType){
        for (i in newTypes!!.indices){
            val newType : NewType = newTypes!!.get(i)
            if(newType === oldType){
                (title_view.getChildAt(i) as TextView).setTextColor(resources.getColor(R.color.select_text_color))
            }else{
                (title_view.getChildAt(i) as TextView).setTextColor(resources.getColor(R.color.text_color))
            }
        }
        mLoadNewsPresenter!!.loadNews(oldType.type)
    }

    override fun setLoadNews(result: ArrayList<NewsDetail>) {
        //成功获取到新闻信息
    }

    override fun loadNewsError(errorType: Int) {
        //获取新闻信息失败
        Toast.makeText(this,errorType.toString(),Toast.LENGTH_SHORT).show()
    }
}
