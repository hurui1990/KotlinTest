package com.example.hurui.news.view

import com.example.hurui.news.bean.MediaBean

/**
 * Created by hurui on 2018/3/18.
 */
interface LoadMediaView {
    fun loadAllPictureMedia(result : HashMap<String, ArrayList<MediaBean>>)
    fun loadAllVideoMedia(result : HashMap<String, ArrayList<MediaBean>>)
    fun loadAllMusicMedia(result : HashMap<String, ArrayList<MediaBean>>)
}