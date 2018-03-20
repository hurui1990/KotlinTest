package com.example.hurui.news.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.news.R
import com.example.hurui.news.activity.PhotoViewActivity
import com.example.hurui.news.adapter.MediaRecyclerAdapter
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.presenter.LoadMediaPresenter
import com.example.hurui.news.view.LoadMediaView
import com.example.hurui.news.view.MyDivider
import kotlinx.android.synthetic.main.fragments_media.*

/**
 * Created by hurui on 2018/3/18.
 */
class MediaFragment : Fragment(), LoadMediaView, MediaRecyclerAdapter.OnItemClickListener {

    var TAG = "MediaFragment"
    var mType : Int? = null
    var mLoadMediaPresenter : LoadMediaPresenter? = null
    var allPicture : ArrayList<MediaBean>? = null
    var mediaAdapter : MediaRecyclerAdapter? = null
    var needload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle : Bundle = arguments
        mType = bundle.getInt("type")
        mLoadMediaPresenter = LoadMediaPresenter(this)

        allPicture  = ArrayList()
        mediaAdapter = MediaRecyclerAdapter(activity!!, mType!!)
        mediaAdapter!!.setData(allPicture!!)
        mediaAdapter!!.setOnItemClickListener(this)
        needload = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View? = inflater!!.inflate(R.layout.fragments_media, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        if(needload) {
            if(mType == 2){
                media_recycler.layoutManager = LinearLayoutManager(activity)
                media_recycler.addItemDecoration(MyDivider(activity, 1))
            }else {
                media_recycler.layoutManager = GridLayoutManager(activity, 4)
            }
            media_recycler.adapter = mediaAdapter
            mLoadMediaPresenter!!.loadAllMedia(mType!!, activity)
            needload = false
        }
    }

    override fun loadAllMedia(resultMap: HashMap<String, ArrayList<MediaBean>>) {
        Log.i(TAG, Thread.currentThread().name)
        allPicture!!.clear()
        for ((k, v) in resultMap){
            Log.i(TAG,v.size.toString())
            allPicture!!.addAll(v)
        }
        Log.i(TAG,"总图片数："+ allPicture!!.size.toString())
        mediaAdapter!!.setData(allPicture!!)
    }

    override fun onItemClick(view: View, position: Int, type : String) {
        if(type.equals("Image")) {
            var pathList: ArrayList<String> = ArrayList()
            for (i in 0..(allPicture!!.size - 1)) {
                pathList.add(allPicture!![i].path)
            }
            var intent: Intent = Intent(activity, PhotoViewActivity::class.java)
            intent.putExtra("position", position)
            intent.putStringArrayListExtra("list", pathList)
            activity.startActivity(intent)
        }else if(type.equals("Video")){
            Log.i("==========", allPicture!![position].path)
        }
    }
}