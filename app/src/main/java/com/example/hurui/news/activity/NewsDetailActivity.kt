package com.example.hurui.news.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_newdetail.*

import com.example.hurui.news.R

/**
 * Created by hurui on 2017/8/3.
 */

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newdetail)

        var intent : Intent = intent
        var url = intent.getStringExtra("url")
        var title = intent.getStringExtra("title")

        newdetail_toolbar.title = title
        newdetail_toolbar.setTitleTextColor(resources.getColor(R.color.white))
        newdetail_toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_open)

        news_detail.loadUrl(url)

        var webSetting : WebSettings = news_detail.settings
        webSetting.javaScriptEnabled = true
        webSetting.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        news_detail.setWebChromeClient(object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress.progress = newProgress
                Log.i("===============", "进度："+newProgress)
                if(newProgress == 100) {
                    progress.visibility = View.GONE
                    var alphaAnim: AlphaAnimation = AlphaAnimation(0f, 1f)
                    alphaAnim.duration = 500
                    news_detail.animation = alphaAnim
                    news_detail.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            overridePendingTransition(R.anim.activity_enter_after,R.anim.activity_out_after)
        }
        return super.onKeyDown(keyCode, event)
    }
}
