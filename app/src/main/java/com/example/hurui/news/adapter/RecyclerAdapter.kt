package com.example.hurui.news.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
//        if(mDataList == null || mDataList.size == 0){
//            return
//        }
        var item: NewsDetail = mDataList!!.get(position) ?: return
        holder?.title?.text = item.title
        holder?.author?.text = item.author_name
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
    }
}