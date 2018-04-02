package com.example.hurui.news.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.services.core.PoiItem
import com.example.hurui.news.R

/**
 * Created by hurui on 2017/8/3.
 */
class PoisAdapter(context: Context) : RecyclerView.Adapter<PoisAdapter.PoiViewHolder>(), View.OnClickListener {

    private var mContext = context
    private var dataList : ArrayList<PoiItem>? = ArrayList()
    private var mOnListItemClick : OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view:View, poi:PoiItem)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: PoiViewHolder?, position: Int) {
        val item : PoiItem = dataList!![position]
        holder!!.name.text = item.toString()
        holder.address.text = item.adName
        holder.itemView.tag = item
        Log.i("PoisAdapter", item.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PoiViewHolder {
        val view : View = LayoutInflater.from(mContext).inflate(R.layout.poi_item, parent, false)
        view.setOnClickListener(this)
        return PoiViewHolder(view)
    }

    fun setOnItemClickListener(listener:OnItemClickListener) {
        mOnListItemClick = listener
    }

    override fun onClick(v: View?) {
        if(mOnListItemClick != null){
            mOnListItemClick!!.onItemClick(v!!, v.tag as PoiItem)
        }
    }

    class PoiViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        var name : TextView = itemView!!.findViewById(R.id.poi_name) as TextView
        var address : TextView = itemView!!.findViewById(R.id.address) as TextView
    }

    fun setData(list:ArrayList<PoiItem>){
        dataList = list
        notifyDataSetChanged()
    }

}