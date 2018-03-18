package com.example.hurui.news.model

import com.example.hurui.news.bean.MediaBean

/**
 * Created by hurui on 2018/3/18.
 */
interface OnLoadMediaListener{
    fun onLoadSuccess(result: HashMap<String, ArrayList<MediaBean>>)
}