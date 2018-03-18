package com.example.hurui.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.news.R
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.view.SquareImageView
import com.squareup.picasso.Picasso
import java.io.File

/**
 * Created by hurui on 2018/3/18.
 */
class MediaRecyclerAdapter(context : Context) : RecyclerView.Adapter<MediaRecyclerAdapter.ItemViewHolder>(){

    val TAG = "MediaRecyclerAdapter"
    private var mContext : Context = context
    private var mDateList : ArrayList<MediaBean>? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder? {
        var view : View = LayoutInflater.from(mContext).inflate(R.layout.media_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDateList!!.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        var item : MediaBean = mDateList!![position]
        Log.i(TAG, item.path)
        holder!!.itemView.tag = item.path
        Picasso.with(mContext).load(File(item.path))
                .resize(300, 300)
                .config(Bitmap.Config.RGB_565)
                .tag("hurui")
                .into(holder.img)
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var img : SquareImageView = itemView!!.findViewById(R.id.item_imageview) as SquareImageView
    }

    fun setData(dataList: ArrayList<MediaBean>){
        mDateList = dataList
        notifyDataSetChanged()
    }
}