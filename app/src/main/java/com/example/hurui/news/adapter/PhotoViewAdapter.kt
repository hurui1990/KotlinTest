package com.example.hurui.news.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.bm.library.PhotoView
import com.squareup.picasso.Picasso
import java.io.File


/**
 * Created by hurui on 2018/3/19.
 */
class PhotoViewAdapter : PagerAdapter{

    private var mImageList : ArrayList<String>? = null
    private var mContext : Context? = null

    constructor(imageUrls : ArrayList<String>, context: Context) : super(){
        mImageList = imageUrls
        mContext = context
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val photoView = PhotoView(mContext)
        Picasso.with(mContext).load(File(mImageList!![position])).into(photoView)
        container!!.addView(photoView)
        return photoView
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mImageList!!.size
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container!!.removeView(`object` as View?)
    }

}