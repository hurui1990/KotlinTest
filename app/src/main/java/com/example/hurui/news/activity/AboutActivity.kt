package com.example.hurui.news.activity

import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.example.hurui.news.R
import com.example.hurui.news.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by hurui on 2017/8/3.
 */

class AboutActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        super.initView()
        about_view.loadUrl("https://hurui1990.github.io/hurui.github.io/about/")

        val webSetting : WebSettings = about_view.settings
        webSetting.javaScriptEnabled = true
        webSetting.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        about_view.webChromeClient = object : WebChromeClient(){
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
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            overridePendingTransition(R.anim.activity_enter_after,R.anim.activity_out_after)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        container.removeAllViews()
        if(about_view != null) {
            about_view.clearHistory()
            about_view.clearCache(true)
            about_view.loadUrl("about:blank") // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            about_view.freeMemory()
            about_view.pauseTimers()
            about_view.destroy()// Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }
}
