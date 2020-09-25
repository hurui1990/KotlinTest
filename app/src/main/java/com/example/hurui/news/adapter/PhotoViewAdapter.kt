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
class PhotoViewAdapter(private val mImageList : ArrayList<String>, private val mContext: Context) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(mContext)
        Picasso.get().load(File(mImageList[position])).into(photoView)
        container!!.addView(photoView)
        return photoView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mImageList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as View?)
    }

}