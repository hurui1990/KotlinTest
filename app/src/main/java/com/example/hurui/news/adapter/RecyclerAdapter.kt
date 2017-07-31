package com.example.hurui.news.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.hurui.news.R
import com.example.hurui.news.bean.NewsDetail

/**
 * Created by hurui on 2017/7/28.
 */
class RecyclerAdapter(context:Context)
    : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {

    var mContext : Context = context
    var mDataList = ArrayList<NewsDetail>()

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        var item: NewsDetail = mDataList!![position]

        holder?.title?.text = item.title
        holder?.author?.text = item.author_name
        holder?.datatime?.text = item.date
        Glide.with(mContext).load(item.thumbnail_pic_s).into(holder?.img)
        Glide.with(mContext).load(item.thumbnail_pic_s02).into(holder?.img2)
        Glide.with(mContext).load(item.thumbnail_pic_s03).into(holder?.img3)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder? {
        var view  = LayoutInflater.from(mContext).inflate(R.layout.list_litem, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setData(dataList: ArrayList<NewsDetail>){
        mDataList = dataList
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title : TextView = itemView?.findViewById(R.id.title) as TextView
        var author : TextView = itemView?.findViewById(R.id.author) as TextView
        var img : ImageView = itemView?.findViewById(R.id.itemImg) as ImageView
        var img2 : ImageView = itemView?.findViewById(R.id.itemImg2) as ImageView
        var img3 : ImageView = itemView?.findViewById(R.id.itemImg3) as ImageView
        var datatime : TextView = itemView?.findViewById(R.id.data_time) as TextView
    }
}