package com.example.hurui.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
        fun onItemClick(view:View, position: Int, type : String)
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
        holder!!.img.tag = item.path
        if(item.type.equals("Image")){
            ImageLoader.build(mContext)!!.loadBitmap(item.path, holder!!.img, requestSize, requestSize)
        }else if(item.type.equals("Video")){
            if(item.path.equals(holder.img.tag)) {
                holder!!.lieanLayout.visibility = View.VISIBLE
                holder!!.txt.text = item.duration
                holder!!.img.setImageBitmap(getVideoThumbnail(item.path, requestSize, requestSize, MediaStore.Images.Thumbnails.MINI_KIND))
            }
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            mOnItemClickListener!!.onItemClick(it, position, item.type)
        })
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mOnItemClickListener = listener
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val img : SquareImageView = itemView!!.findViewById(R.id.item_imageview) as SquareImageView
        val lieanLayout : LinearLayout = itemView!!.findViewById(R.id.video_info) as LinearLayout
        val txt : TextView = itemView!!.findViewById(R.id.video_duration_txt) as TextView
    }

    fun setData(dataList: ArrayList<MediaBean>){
        mDateList = dataList
        notifyDataSetChanged()
    }

    private fun getVideoThumbnail(videoPath : String, width : Int, height : Int, kind : Int) : Bitmap{
        var bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind)
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
        return bitmap
    }
}