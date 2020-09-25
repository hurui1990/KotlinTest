package com.example.hurui.news.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.example.hurui.news.R
import kotlinx.android.synthetic.main.activity_newdetail.*

/**
 * Created by hurui on 2017/8/3.
 */

class NewsDetailActivity : AppCompatActivity() {

    private val url by lazy { intent.getStringExtra("url") }
    private val title by lazy { intent.getStringExtra("title") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newdetail)

        newdetail_toolbar.setTitleTextColor(resources.getColor(R.color.white))
        newdetail_toolbar.title = title
        newdetail_toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_keyboard_arrow_left)
        setSupportActionBar(newdetail_toolbar)

        news_detail.loadUrl(url!!)

        val webSetting : WebSettings = news_detail.settings
        webSetting.javaScriptEnabled = true
        webSetting.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        news_detail.setWebChromeClient(object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress.progress = newProgress
                if(newProgress == 100) {
                    progress.visibility = View.GONE
                    val alphaAnim = AlphaAnimation(0f, 1f)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            R.id.share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, url)
                intent.putExtra(Intent.EXTRA_SUBJECT, title)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(Intent.createChooser(intent, "分享到"))
            }
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.activity_enter_after,R.anim.activity_out_after)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
