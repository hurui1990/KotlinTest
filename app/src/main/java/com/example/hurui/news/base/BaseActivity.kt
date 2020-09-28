package com.example.hurui.news.base

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import kotlin.system.exitProcess

/**
 *  Created By RuiHu At 2020/9/10 11:20
 */
abstract class BaseActivity : AppCompatActivity() {

    private var isExit: Boolean = false

    private val permissionMap  by lazy { hashMapOf(

        "com.example.hurui.news.activity.MediasActivity" to arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ),

    ) }

    protected val permissions by lazy { permissionMap[this.javaClass.name]!! }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(getLayoutId())
        initView()
        initData()
    }

    abstract fun getLayoutId() : Int

    protected open fun fullScreen(){
        if (Build.VERSION.SDK_INT >= 28){
            // 延伸显示区域到刘海
            val lp: WindowManager.LayoutParams = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
            // 设置页面全屏显示
            val decorView: View = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    protected open fun initView(){
        actionBar?.hide()
    }

    protected open fun initData(){}

    protected fun myToast(str: String){
        runOnUiThread { toast(str) }
    }

    protected inline fun <reified T : BaseActivity> startActivityAndFinish(){
        startActivity<T>()
        finish()
    }

    protected fun hideTitleBar(){
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun exitBy2Click() {
        val handler = Handler()
        if ((!isExit)) {
            isExit = true
            myToast("再按一次退出APP")
            handler.postDelayed({ isExit = false }, 1000 * 2) //x秒后没按就取消
        } else {
            finish()
            exitProcess(0)
        }
    }
}