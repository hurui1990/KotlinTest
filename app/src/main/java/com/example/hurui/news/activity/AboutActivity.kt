package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.example.hurui.news.R
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by hurui on 2017/8/3.
 */

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        about_view.loadUrl("https://hurui1990.github.io/hurui.github.io/about/")

        val webSetting : WebSettings = about_view.settings
        webSetting.javaScriptEnabled = true
        webSetting.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        about_view.setWebChromeClient(object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressbar.progress = newProgress
                if(newProgress == 100) {
                    progressbar.visibility = View.GONE
                    val alphaAnim = AlphaAnimation(0f, 1f)
                    alphaAnim.duration = 500
                    about_view.animation = alphaAnim
                    about_view.visibility = View.VISIBLE
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
