package com.example.hurui.news.utils

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.view.Display
import android.view.WindowManager

/**
 * Created by hurui on 2017/5/25.
 */
class Utils {

    companion object {

        public val NO_INERNET_CONNECT : Int = 1

        fun getScreenWidth(context : Context): Int {
            val windowManager : WindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
            val display : Display = windowManager.defaultDisplay
            val screenWidth = display.width
            return screenWidth
        }
    }
}