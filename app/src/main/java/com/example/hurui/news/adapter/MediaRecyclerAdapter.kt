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
import com.example.hurui.news.utils.Constans
import com.example.hurui.news.utils.ImageLoader
import com.example.hurui.news.utils.Utils
import com.example.hurui.news.view.SquareImageView

/**
 * Created by hurui on 2018/3/18.
 */
class MediaRecyclerAdapter(private val mContext : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var mDateList : ArrayList<MediaBean>
    private val requestSize : Int = Utils.calculateImageviewSize(mContext)
    private lateinit var mOnItemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(view:View, position: Int, type : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View?
        return if(viewType == 2){
            view = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false)
            MusicViewHolder(view)
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.media_item, parent, false)
            MediaViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mDateList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item : MediaBean = mDateList[position]
        with(holder){
            when(this){
                is MediaViewHolder -> {
                    val mediaHolder : MediaViewHolder = this
                    mediaHolder.img.tag = item.path
                    mediaHolder.lieanLayout.visibility = View.VISIBLE
                    if (item.type == Constans.MEDIA_TYPE_IMAGE) {
                        if (item.path == this.img.tag) {
                            mediaHolder.txt.text = "${item.list.size}张图片"
                            ImageLoader.build(mContext)!!.loadBitmap(item.path, mediaHolder.img, requestSize, requestSize)
                        }
                    } else if (item.type == Constans.MEDIA_TYPE_VEDIO) {
                        if (item.path == this.img.tag) {
                            mediaHolder.txt.text = "${item.duration}个视频"
                            mediaHolder.img.setImageBitmap(getVideoThumbnail(item.path, requestSize, requestSize, MediaStore.Images.Thumbnails.MINI_KIND))
                        }
                    }
                }
                is MusicViewHolder -> {
                    val musicHolder: MusicViewHolder = this
                    musicHolder.txtTitle.tag = item.path
                    musicHolder.txtTitle.text = item.title
                    musicHolder.txtSinger.text = item.singer
                }
            }
        }
        holder!!.itemView.setOnClickListener {
            mOnItemClickListener!!.onItemClick(it, position, item.type)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(mDateList!![position].type == Constans.MEDIA_TYPE_MUSIC){
            2
        }else{
            1
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mOnItemClickListener = listener
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img : SquareImageView = itemView!!.findViewById(R.id.item_imageview)
        val lieanLayout : LinearLayout = itemView!!.findViewById(R.id.video_info)
        val txt : TextView = itemView!!.findViewById(R.id.video_duration_txt)
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle : TextView = itemView!!.findViewById(R.id.title)
        val txtSinger : TextView = itemView!!.findViewById(R.id.singer)
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