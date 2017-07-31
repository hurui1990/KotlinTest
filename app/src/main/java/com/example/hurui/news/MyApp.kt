package com.example.hurui.news

import android.app.Application
import android.util.Log

/**
 * Created by hurui on 2017/7/31.
 */

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("application", "onCreate")
    }
}
