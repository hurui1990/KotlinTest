package com.example.hurui.news.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.hurui.news.R
import com.example.hurui.news.bean.NewsDetail
import com.squareup.picasso.Picasso

/**
 * Created by hurui on 2017/7/28.
 */
class RecyclerAdapter(context:Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mContext : Context = context
    var mDataList = ArrayList<NewsDetail>()
    var type0 = 0
    var type1 = 1
    var type2 = 2
    var type3 = 3

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var item: NewsDetail = mDataList[position]
        when(getItemViewType(position)){
            type0 -> {
                holder!!.itemView.visibility = View.GONE
            }
            type1 -> {
                var holderOne : ItemViewHolderOne = holder as ItemViewHolderOne
                holderOne.title.text = item.title
                holderOne.author.text = item.author_name
                holderOne.datatime.text = item.date
                Picasso.with(mContext).load(item.thumbnail_pic_s).into(holderOne.img)
            }
            type2 -> {
                var holderTwo : ItemViewHolderTwo = holder as ItemViewHolderTwo
                holderTwo.title.text = item.title
                holderTwo.author.text = item.author_name
                holderTwo.datatime.text = item.date
                Picasso.with(mContext).load(item.thumbnail_pic_s).into(holderTwo.img)
                Picasso.with(mContext).load(item.thumbnail_pic_s02).into(holderTwo.img2)
            }
            type3 -> {
                var holderThree : ItemViewHolderThree = holder as ItemViewHolderThree
                holderThree.title.text = item.title
                holderThree.author.text = item.author_name
                holderThree.datatime.text = item.date
                Picasso.with(mContext).load(item.thumbnail_pic_s).into(holderThree.img)
                Picasso.with(mContext).load(item.thumbnail_pic_s02).into(holderThree.img2)
                Picasso.with(mContext).load(item.thumbnail_pic_s03).into(holderThree.img3)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        Log.i("viewType", viewType.toString())
        var view : View
        if(viewType == type3){
            view  = LayoutInflater.from(mContext).inflate(R.layout.list_litem_three, parent, false)
            return ItemViewHolderThree(view)
        }else if(viewType == type2){
            view  = LayoutInflater.from(mContext).inflate(R.layout.list_litem_two, parent, false)
            return ItemViewHolderTwo(view)
        }else{
            view  = LayoutInflater.from(mContext).inflate(R.layout.list_litem_one, parent, false)
            return ItemViewHolderOne(view)
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        var item = mDataList[position]
        var picPaths = ArrayList<String>()
        if(item.thumbnail_pic_s != null){
            picPaths.add(item.thumbnail_pic_s)
        }
        if(item.thumbnail_pic_s02!= null){
            picPaths.add(item.thumbnail_pic_s02)
        }
        if(item.thumbnail_pic_s03 != null){
            picPaths.add(item.thumbnail_pic_s03)
        }
        when(picPaths.size){
            1 -> {return type1}
            2 -> {return type2}
            3 -> {return type3}
            else -> {return type0}
        }
    }

    fun setData(dataList: ArrayList<NewsDetail>){
        mDataList = dataList
        notifyDataSetChanged()
    }

    class ItemViewHolderThree(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title : TextView = itemView!!.findViewById(R.id.title) as TextView
        var author : TextView = itemView!!.findViewById(R.id.author) as TextView
        var img : ImageView = itemView!!.findViewById(R.id.itemImg) as ImageView
        var img2 : ImageView = itemView!!.findViewById(R.id.itemImg2) as ImageView
        var img3 : ImageView = itemView!!.findViewById(R.id.itemImg3) as ImageView
        var datatime : TextView = itemView!!.findViewById(R.id.data_time) as TextView
    }

    class ItemViewHolderTwo(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title : TextView = itemView!!.findViewById(R.id.title) as TextView
        var author : TextView = itemView!!.findViewById(R.id.author) as TextView
        var img : ImageView = itemView!!.findViewById(R.id.itemImg) as ImageView
        var img2 : ImageView = itemView!!.findViewById(R.id.itemImg2) as ImageView
        var datatime : TextView = itemView!!.findViewById(R.id.data_time) as TextView
    }

    class ItemViewHolderOne(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var title : TextView = itemView!!.findViewById(R.id.title) as TextView
        var author : TextView = itemView!!.findViewById(R.id.author) as TextView
        var img : ImageView = itemView!!.findViewById(R.id.itemImg) as ImageView
        var datatime : TextView = itemView!!.findViewById(R.id.data_time) as TextView
    }
}