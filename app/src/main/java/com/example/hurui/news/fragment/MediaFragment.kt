package com.example.hurui.news.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
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
import com.example.hurui.news.utils.Constans
import com.example.hurui.news.utils.PermissionUtil
import com.example.hurui.news.view.LoadMediaView
import com.example.hurui.news.view.MyDivider
import kotlinx.android.synthetic.main.fragments_media.*

/**
 * Created by hurui on 2018/3/18.
 */
class MediaFragment : Fragment(), LoadMediaView, MediaRecyclerAdapter.OnItemClickListener {

    private var TAG = "MediaFragment"
    private val mType by lazy { arguments!!.getInt("type") }
    private lateinit var mLoadMediaPresenter : LoadMediaPresenter
    private lateinit var allPicture : ArrayList<MediaBean>
    private lateinit var mediaAdapter : MediaRecyclerAdapter
    private var needload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoadMediaPresenter = LoadMediaPresenter(this)

        allPicture  = ArrayList()
        mediaAdapter = MediaRecyclerAdapter(activity!!)
        mediaAdapter.setData(allPicture!!)
        mediaAdapter.setOnItemClickListener(this)
        needload = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragments_media, container, false)
    }

    override fun onResume() {
        super.onResume()
        if(needload) {
            when (mType) {
                2 -> {
                    media_recycler.layoutManager = LinearLayoutManager(activity)
                    media_recycler.addItemDecoration(MyDivider(activity, 1))
                }
                1 -> {
                    media_recycler.layoutManager = GridLayoutManager(activity, 3)
                }
                0 -> {
                    media_recycler.layoutManager = GridLayoutManager(activity, 2)
                }
            }
            media_recycler.adapter = mediaAdapter
            mLoadMediaPresenter.loadAllMedia(mType, activity!!)
            needload = false
        }
    }

    override fun loadAllPictureMedia(resultMap: HashMap<String, ArrayList<MediaBean>>) {
        Log.i(TAG, Thread.currentThread().name)
        allPicture.clear()
        for ((k, v) in resultMap){
            Log.i(TAG,v.size.toString())
            var bean = MediaBean()
            bean.type = Constans.MEDIA_TYPE_IMAGE
            bean.name = k
            bean.path = v[0].path
            bean.list = v
            allPicture.add(bean)
        }
        mediaAdapter.setData(allPicture)
    }

    override fun loadAllVideoMedia(result: HashMap<String, ArrayList<MediaBean>>) {
        Log.i(TAG, Thread.currentThread().name)
        allPicture.clear()
        for ((k, v) in result){
            Log.i(TAG,v.size.toString())
            var bean = MediaBean()
            bean.type = Constans.MEDIA_TYPE_VEDIO
            bean.name = k
            bean.duration = v.size.toString()
            bean.path = v[0].path
            bean.list = v
            allPicture.add(bean)

        }
        mediaAdapter.setData(allPicture!!)
    }

    override fun loadAllMusicMedia(result: HashMap<String, ArrayList<MediaBean>>) {
        Log.i(TAG, Thread.currentThread().name)
        allPicture.clear()
        for ((k, v) in result){
            Log.i(TAG,v.size.toString())
            allPicture.addAll(v)
        }
        mediaAdapter.setData(allPicture!!)
    }

    override fun onItemClick(view: View, position: Int, type : String) {
        if(type == Constans.MEDIA_TYPE_IMAGE) {
            val pathList: ArrayList<String> = ArrayList()
            for (i in 0 until allPicture!!.size) {
                pathList.add(allPicture[i].path)
            }
            val intent = Intent(activity, PhotoViewActivity::class.java)
            intent.putExtra("position", position)
            intent.putStringArrayListExtra("list", pathList)
            activity!!.startActivity(intent)
        }else if(type == Constans.MEDIA_TYPE_VEDIO){
            Log.i("==========", allPicture!![position].path)
        }
    }
}