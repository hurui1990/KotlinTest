package com.example.hurui.kotlintest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hurui.kotlintest.R
import com.example.hurui.kotlintest.bean.User
import kotlinx.android.synthetic.main.list_item.view.*


/**
 * Created by hurui on 2017/5/22.
 */
class MyAdapter(var dataList : ArrayList<User>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onBindViewHolder(viewHolder : ViewHolder?, position: Int) {
        var user : User = dataList.get(position)
        viewHolder?.view?.name?.text = user.name
        viewHolder?.view?.age?.text = user.age.toString()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, posotion: Int): ViewHolder {
        var view : View = LayoutInflater.from(viewGroup!!.context).inflate(R.layout.list_item,null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewHolder(var view : View?) : RecyclerView.ViewHolder(view)
}