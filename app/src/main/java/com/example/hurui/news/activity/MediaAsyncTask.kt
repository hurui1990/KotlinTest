package com.example.hurui.news.activity

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.model.OnLoadMediaListener
import com.example.hurui.news.utils.Utils
import java.io.File

/**
 * Created by hurui on 2018/3/18.
 */
class MediaAsyncTask(context: Context, onLoadMediaListener: OnLoadMediaListener, type : Int): AsyncTask<ContentResolver, Void, HashMap<String, ArrayList<MediaBean>>>() {

    val TAG = "MediaAsyncTask"
    val mContext : Context = context
    val mOnLoadMediaListener : OnLoadMediaListener = onLoadMediaListener
    val mType : Int? = type

    override fun doInBackground(vararg params: ContentResolver?): HashMap<String, ArrayList<MediaBean>> {
        when(mType){
            0 -> {
                return getAllPicture()
            }
            1 -> {
                return getAllVedio()
            }
        }
        return null!!
    }

    override fun onPostExecute(result: HashMap<String, ArrayList<MediaBean>>?) {
        super.onPostExecute(result)
        mOnLoadMediaListener.onLoadSuccess(result!!)
    }

    private fun getAllPicture() : HashMap<String, ArrayList<MediaBean>>{
        Log.i(TAG, "获取所有图片信息")
        var mMediaBeanList = ArrayList<MediaBean>()
        val allPhotosTemp = HashMap<String, ArrayList<MediaBean>>()//所有照片
        var mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var projImage : Array<String> = arrayOf(MediaStore.Images.Media._ID
                , MediaStore.Images.Media.DATA
                , MediaStore.Images.Media.SIZE
                , MediaStore.Images.Media.DISPLAY_NAME)
        var mCursor : Cursor = mContext.contentResolver.query(mImageUri,
                projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                arrayOf("image/jpeg", "image/png"),
                MediaStore.Images.Media.DATE_MODIFIED+" desc")
        if(mCursor!=null){
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                var path : String = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                var size : Int = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE))/1024
                var displayName : String  = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                //用于展示相册初始化界面
//                mMediaBeanList!!.add(MediaBean("Image", path, displayName, size.toString(), "", ""))
                // 获取该图片的父路径名
                var dirPath : String = File(path).parentFile.absolutePath
                //存储对应关系
                if (allPhotosTemp.containsKey(dirPath)) {
                    var data : ArrayList<MediaBean>? = allPhotosTemp!![dirPath]
                    data!!.add(MediaBean("Image",path, displayName, size.toString(),"", ""))
                    continue
                } else {
                    var data : ArrayList<MediaBean>  = ArrayList<MediaBean>()
                    data.add(MediaBean("Image",path,displayName,size.toString(),"",""))
                    allPhotosTemp[dirPath] = data
                }
            }
            mCursor.close()
        }
        return allPhotosTemp
    }

    private fun getAllVedio() : HashMap<String, ArrayList<MediaBean>>{
        Log.i(TAG, "获取所有视频信息")
        var mMediaBeanList = ArrayList<MediaBean>()
        val allPhotosTemp = HashMap<String, ArrayList<MediaBean>>()
        val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projVideo : Array<String> = arrayOf(MediaStore.Video.Thumbnails._ID
                , MediaStore.Video.Thumbnails.DATA
                ,MediaStore.Video.Media.DURATION
                ,MediaStore.Video.Media.SIZE
                ,MediaStore.Video.Media.DISPLAY_NAME
                ,MediaStore.Video.Media.DATE_MODIFIED)
        val mCursor : Cursor = mContext.contentResolver.query(videoUri,
                projVideo,
                MediaStore.Video.Media.MIME_TYPE + "=?",
                arrayOf("video/mp4"),
                MediaStore.Video.Media.DATE_MODIFIED+" desc")
        if(mCursor != null){
            while (mCursor.moveToNext()){
                // 获取视频的路径
                val videoId : Int = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID))
                val path : String = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val duration : Int = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                var size : Long = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.SIZE))/1024
                if(size<0){
                    size = File(path).length()/1024
                }
                val displayName : String = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                val modifyTime : Long = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))

                //提前生成缩略图
                MediaStore.Video.Thumbnails.getThumbnail(mContext.contentResolver, videoId.toLong(), MediaStore.Video.Thumbnails.MICRO_KIND, null)
                val projection : Array<String> = arrayOf(MediaStore.Video.Thumbnails._ID, MediaStore.Video.Thumbnails.DATA)
                val cursor : Cursor = mContext.contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
                        , projection
                        , MediaStore.Video.Thumbnails.VIDEO_ID + "=?"
                        , arrayOf(videoId.toString())
                        , null)
                var thumbPath = ""
                while (cursor.moveToNext()){
                    thumbPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA))
                }
                cursor.close()
                // 获取该视频的父路径名
                val dirPath : String = File(path).parentFile.absolutePath
                //存储对应关系
                if (allPhotosTemp.containsKey(dirPath)) {
                    var data : ArrayList<MediaBean>? = allPhotosTemp[dirPath]
                    data!!.add(MediaBean("Video",path, displayName,size.toString(),thumbPath, Utils.timeParse(duration.toLong())))
                    continue
                } else {
                    var data : ArrayList<MediaBean> =  ArrayList()
                    data.add(MediaBean("Video",path, displayName,size.toString(),thumbPath, Utils.timeParse(duration.toLong())))
                    allPhotosTemp[dirPath] = data
                }
            }
            mCursor.close()
        }
        return allPhotosTemp
    }
}