package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.news.R
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.presenter.LoadMediaPresenter
import com.example.hurui.news.view.LoadMediaView

/**
 * Created by hurui on 2018/3/18.
 */
class MediaFragment : Fragment(), LoadMediaView{

    var TAG = "MediaFragment"
    var mType : Int? = null
    var mLoadMediaPresenter : LoadMediaPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle : Bundle = arguments
        mType = bundle.getInt("type")

        mLoadMediaPresenter = LoadMediaPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater!!.inflate(R.layout.fragments_media, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        mLoadMediaPresenter!!.loadAllMedia(mType!!)
    }

    override fun loadAllMedia(result: ArrayList<MediaBean>) {

    }
}