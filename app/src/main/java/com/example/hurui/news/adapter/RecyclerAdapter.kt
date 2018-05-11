package com.example.hurui.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.hurui.news.R
import com.example.hurui.news.bean.NewsDetail
import com.example.hurui.news.utils.Utils
import com.squareup.picasso.Picasso

/**
 * Created by hurui on 2017/7/28.
 */
class RecyclerAdapter(private val mContext : Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
    private var mDataList = ArrayList<NewsDetail>()
    private var type0 = 0
    private var type1 = 1
    private var type2 = 2
    private var type3 = 3
    private lateinit var onItemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(view:View, position:Int)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val screenWidth : Int = Utils.getScreenWidth(mContext)
        val item: NewsDetail = mDataList[position]
        holder!!.itemView.tag = position
        holder.itemView.setOnClickListener(this)
        when(getItemViewType(position)){
            type0 -> {
                holder.itemView.visibility = View.GONE
            }
            type1 -> {
                val holderOne : ItemViewHolderOne = holder as ItemViewHolderOne
                holderOne.title.text = item.title
                holderOne.author.text = item.author_name
                holderOne.datatime.text = item.date
                Picasso.with(mContext)
                        .load(item.thumbnail_pic_s)
                        .config(Bitmap.Config.RGB_565)
                        .resize(screenWidth/3, screenWidth/4)
                        .into(holderOne.img)
            }
            type2 -> {
                val holderTwo : ItemViewHolderTwo = holder as ItemViewHolderTwo
                holderTwo.title.text = item.title
                holderTwo.author.text = item.author_name
                holderTwo.datatime.text = item.date
                Picasso.with(mContext)
                        .load(item.thumbnail_pic_s)
                        .config(Bitmap.Config.RGB_565)
                        .resize(screenWidth/2, screenWidth*3/10)
                        .into(holderTwo.img)
                Picasso.with(mContext)
                        .load(item.thumbnail_pic_s02)
                        .config(Bitmap.Config.RGB_565)
                        .resize(screenWidth/2, screenWidth*3/10)
                        .into(holderTwo.img2)
            }
            type3 -> {
                val holderThree : ItemViewHolderThree = holder as ItemViewHolderThree
                holderThree.title.text = item.title
                holderThree.author.text = item.author_name
                holderThree.datatime.text = item.date
                Picasso.with(mContext)
                        .load(item.thumbnail_pic_s)
                        .config(Bitmap.Config.RGB_565)
                        .resize(screenWidth/3, screenWidth*3/15)
                        .into(holderThree.img)
                Picasso.with(mContext)
                        .load(item.thumbnail_pic_s02)
                        .config(Bitmap.Config.RGB_565)
                        .resize(screenWidth/3, screenWidth*3/15)
                        .into(holderThree.img2)
                Picasso.with(mContext)
                        .load(item.thumbnail_pic_s03)
                        .config(Bitmap.Config.RGB_565)
                        .resize(screenWidth/3, screenWidth*3/15)
                        .into(holderThree.img3)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        Log.i("viewType", viewType.toString())
        val view : View
        return when (viewType) {
            type3 -> {
                view  = LayoutInflater.from(mContext).inflate(R.layout.list_litem_three, parent, false)
                ItemViewHolderThree(view)
            }
            type2 -> {
                view  = LayoutInflater.from(mContext).inflate(R.layout.list_litem_two, parent, false)
                ItemViewHolderTwo(view)
            }
            else -> {
                view  = LayoutInflater.from(mContext).inflate(R.layout.list_litem_one, parent, false)
                ItemViewHolderOne(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = mDataList[position]
        val picPaths = ArrayList<String>()
        if(item.thumbnail_pic_s != null){
            picPaths.add(item.thumbnail_pic_s)
        }
        if(item.thumbnail_pic_s02!= null){
            picPaths.add(item.thumbnail_pic_s02)
        }
        if(item.thumbnail_pic_s03 != null){
            picPaths.add(item.thumbnail_pic_s03)
        }
        return when(picPaths.size){
            1 -> {
                type1
            }
            2 -> {
                type2
            }
            3 -> {
                type3
            }
            else -> {
                type0
            }
        }
    }

    override fun onClick(v: View?) {
        if(onItemClickListener != null){
            onItemClickListener!!.onItemClick(v!!, v.tag as Int)
        }
    }

    fun setItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
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