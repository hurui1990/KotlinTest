package com.example.hurui.news.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.hurui.news.R
import com.example.hurui.news.bean.MenuType



/**
 * Created by hurui on 2017/7/31.
 */
class DrawerListAdapter(context:Context, itemList:ArrayList<MenuType>)
    : RecyclerView.Adapter<DrawerListAdapter.DrawItemHolder>(), View.OnClickListener {

    var mContext = context
    var datalist = itemList
    var mOnItemClickListener: OnItemClickListener? = null


    //define interface
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onBindViewHolder(holder: DrawItemHolder?, position: Int) {
        var item : MenuType = datalist!![position]
        holder!!.img.setImageResource(item.image)
        holder!!.text.text = item.itemtext
        holder.itemView.tag = position
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DrawItemHolder {
        var itemView = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_item, parent, false)
        itemView!!.setOnClickListener(this)
        return DrawItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onClick(v: View?) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener!!.onItemClick(v!!, v.tag as Int)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    class DrawItemHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var img : ImageView = itemView.findViewById(R.id.drawer_menu_img) as ImageView
        var text : TextView = itemView.findViewById(R.id.drawer_menu_text) as TextView
    }
}