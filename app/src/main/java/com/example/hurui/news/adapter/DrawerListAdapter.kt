package com.example.hurui.news.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hurui.news.R
import com.example.hurui.news.bean.MenuType

/**
 * Created by hurui on 2017/7/31.
 */
class DrawerListAdapter(context:Context, itemList:ArrayList<MenuType>)
    : RecyclerView.Adapter<DrawerListAdapter.DrawItemHolder>() {

    var mContext = context
    var datalist = itemList

    override fun onBindViewHolder(holder: DrawItemHolder?, position: Int) {
        var item : MenuType = datalist!![position]
        holder!!.img.setImageResource(item.image)
        holder!!.text.text = item.itemtext

        holder.itemView.setOnClickListener(View.OnClickListener {
            //TODO:点击跳转到各个界面
            Toast.makeText(mContext, item.itemtext, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DrawItemHolder {
        var itemView = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_item, parent, false)
        return DrawItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    class DrawItemHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var img : ImageView = itemView.findViewById(R.id.drawer_menu_img) as ImageView
        var text : TextView = itemView.findViewById(R.id.drawer_menu_text) as TextView
    }
}