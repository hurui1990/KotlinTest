package com.example.hurui.news.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.news.R
import com.example.hurui.news.adapter.MediaRecyclerAdapter
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.presenter.LoadMediaPresenter
import com.example.hurui.news.view.LoadMediaView
import kotlinx.android.synthetic.main.fragments_media.*

/**
 * Created by hurui on 2018/3/18.
 */
class MediaFragment : Fragment(), LoadMediaView {

    var TAG = "MediaFragment"
    var mType : Int? = null
    var mLoadMediaPresenter : LoadMediaPresenter? = null
    var allPicture : ArrayList<MediaBean>? = null
    var mediaAdapter : MediaRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle : Bundle = arguments
        mType = bundle.getInt("type")

        mLoadMediaPresenter = LoadMediaPresenter(this)

        allPicture  = ArrayList()
        mediaAdapter = MediaRecyclerAdapter(activity)
        mediaAdapter!!.setData(allPicture!!)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater!!.inflate(R.layout.fragments_media, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        media_recycler.layoutManager = GridLayoutManager(activity, 4) as RecyclerView.LayoutManager?
        media_recycler.adapter = mediaAdapter
        mLoadMediaPresenter!!.loadAllMedia(mType!!, activity)

    }

    override fun loadAllMedia(resultMap: HashMap<String, ArrayList<MediaBean>>) {
        Log.i(TAG, Thread.currentThread().name)
        for ((k, v) in resultMap){
            Log.i(TAG,v.size.toString())
            allPicture!!.addAll(v)
        }
        Log.i(TAG,"总图片数："+ allPicture!!.size.toString())
        mediaAdapter!!.setData(allPicture!!)
    }
}