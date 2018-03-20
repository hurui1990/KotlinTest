package com.example.hurui.news.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.news.R
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.utils.ImageLoader
import com.example.hurui.news.utils.Utils
import com.example.hurui.news.view.SquareImageView

/**
 * Created by hurui on 2018/3/18.
 */
class MediaRecyclerAdapter(context : Context) : RecyclerView.Adapter<MediaRecyclerAdapter.ItemViewHolder>(){
    val TAG = "MediaRecyclerAdapter"
    private var mContext : Context = context
    private var mDateList : ArrayList<MediaBean>? = null
    private var mScrollState : Boolean = false
    private val requestSize : Int = Utils.calculateImageviewSize(mContext!!)
    private var mOnItemClickListener : OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(view:View, position: Int)
    }

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
        ImageLoader.build(mContext)!!.loadBitmap(item.path, holder!!.img, requestSize, requestSize)
        holder.itemView.setOnClickListener(View.OnClickListener {
            mOnItemClickListener!!.onItemClick(it,position)
        })
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mOnItemClickListener = listener
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var img : SquareImageView = itemView!!.findViewById(R.id.item_imageview) as SquareImageView
    }

    fun setData(dataList: ArrayList<MediaBean>){
        mDateList = dataList
        notifyDataSetChanged()
    }

    fun setScrollState(scrollState : Boolean){
        mScrollState = scrollState
    }
}