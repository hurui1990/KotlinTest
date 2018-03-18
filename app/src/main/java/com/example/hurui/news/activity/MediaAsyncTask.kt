package com.example.hurui.news.activity

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import com.example.hurui.news.bean.MediaBean
import com.example.hurui.news.model.OnLoadMediaListener
import java.io.File

/**
 * Created by hurui on 2018/3/18.
 */
class MediaAsyncTask(context: Context, onLoadMediaListener: OnLoadMediaListener): AsyncTask<ContentResolver, Void, HashMap<String, ArrayList<MediaBean>>>() {

    val TAG = "MediaAsyncTask"
    val mContext : Context = context
    val mOnLoadMediaListener : OnLoadMediaListener = onLoadMediaListener

    override fun doInBackground(vararg params: ContentResolver?): HashMap<String, ArrayList<MediaBean>> {
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
                mMediaBeanList!!.add(MediaBean("Image", path, size.toString(), displayName))
                // 获取该图片的父路径名
                var dirPath : String = File(path).parentFile.absolutePath
                //存储对应关系
                if (allPhotosTemp.containsKey(dirPath)) {
                    var data : ArrayList<MediaBean>? = allPhotosTemp!![dirPath]
                    data!!.add(MediaBean("Image",path,size.toString(),displayName))
                    continue
                } else {
                    var data : ArrayList<MediaBean>  = ArrayList<MediaBean>()
                    data.add(MediaBean("Image",path,size.toString(),displayName))
                    allPhotosTemp.put(dirPath,data)
                }
            }
            mCursor.close()
        }
        return allPhotosTemp
    }

    override fun onPostExecute(result: HashMap<String, ArrayList<MediaBean>>?) {
        super.onPostExecute(result)
        mOnLoadMediaListener.onLoadSuccess(result!!)
    }

}