package com.example.hurui.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
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
class MediaRecyclerAdapter(context : Context, type : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val TAG = "MediaRecyclerAdapter"
    private var mContext : Context = context
    private var mDateList : ArrayList<MediaBean>? = null
    private var mType : Int = type
    private val requestSize : Int = Utils.calculateImageviewSize(mContext)
    private var mOnItemClickListener : OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(view:View, position: Int, type : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var view : View? = null
        return if(viewType == 2){
            view = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false)
            MusicViewHolder(view)
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.media_item, parent, false)
            MediaViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mDateList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var item : MediaBean = mDateList!![position]
        if(holder is MediaViewHolder) {
            val mediaHolder : MediaViewHolder = holder
            mediaHolder.img.tag = item.path
            if (item.type.equals(Constans.MEDIA_TYPE_IMAGE)) {
                ImageLoader.build(mContext)!!.loadBitmap(item.path, mediaHolder.img, requestSize, requestSize)
            } else if (item.type.equals(Constans.MEDIA_TYPE_VEDIO)) {
                if (item.path.equals(holder.img.tag)) {
                    mediaHolder.lieanLayout.visibility = View.VISIBLE
                    mediaHolder.txt.text = item.duration
                    mediaHolder.img.setImageBitmap(getVideoThumbnail(item.path, requestSize, requestSize, MediaStore.Images.Thumbnails.MINI_KIND))
                }
            }
        }else if(holder is MusicViewHolder) {
            val musicHolder: MusicViewHolder = holder
            musicHolder.txtTitle.tag = item.path
            musicHolder.txtTitle.text = item.title
            musicHolder.txtSinger.text = item.singer
        }
        holder!!.itemView.setOnClickListener(View.OnClickListener {
            mOnItemClickListener!!.onItemClick(it, position, item.type)
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if(mDateList!![position].type.equals(Constans.MEDIA_TYPE_MUSIC)){
            2
        }else{
            1
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mOnItemClickListener = listener
    }

    class MediaViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val img : SquareImageView = itemView!!.findViewById(R.id.item_imageview) as SquareImageView
        val lieanLayout : LinearLayout = itemView!!.findViewById(R.id.video_info) as LinearLayout
        val txt : TextView = itemView!!.findViewById(R.id.video_duration_txt) as TextView
    }

    class MusicViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val txtTitle : TextView = itemView!!.findViewById(R.id.title) as TextView
        val txtSinger : TextView = itemView!!.findViewById(R.id.singer) as TextView
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