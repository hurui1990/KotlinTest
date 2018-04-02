package com.example.hurui.news.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.*
import android.util.LruCache
import com.example.hurui.news.bean.BitmapBean
import com.example.hurui.news.view.SquareImageView
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by hurui on 2018/3/19.
 * 图片二级缓存加载类
 */

class ImageLoader(private val mContext: Context) {
    private val mMemoryCache: LruCache<String, Bitmap>
    private var mDiskLruCache : DiskLruCache? = null
    private val DISK_CACHE_SIZE : Long = 1024 * 1024 * 50
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = CPU_COUNT + 1
    private val MAXIMUM_POOL_EXECUTOR = CPU_COUNT * 5 + 1
    private val KEPP_ALIVE = 10L

    private val mHandler : Handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message?) {
            when (msg!!.what){
                0 -> {
                    val result : BitmapBean = msg.obj as BitmapBean
                    val imageView : SquareImageView = result.imageview
                    val bitmap : Bitmap = result.bitmap
                    val path : String = result.path
                    if(path == imageView.tag){
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private val mThreadFactory : ThreadFactory = object : ThreadFactory {
        private val mCount : AtomicInteger = AtomicInteger()
        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "ImageLoad#"+mCount.getAndIncrement())
        }
    }

    private val THREAD_POOL_EXECUTOR : Executor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_EXECUTOR, KEPP_ALIVE,TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(), mThreadFactory)

    init {
        mMemoryCache = object : LruCache<String, Bitmap>(100) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.rowBytes * value.height / 1024
            }
        }

        val diskCacheDir = getDiskCacheDir(mContext, "bitmap")
        if (!diskCacheDir.exists()){
            diskCacheDir.mkdirs()
        }
        if(getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE){
            mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1 , DISK_CACHE_SIZE)
        }
    }

    private fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap)
        }
    }

    private fun getBitmapFromMemoryCache(key: String): Bitmap? {
        return mMemoryCache.get(key)
    }

    fun loadBitmap(path : String, imageView : SquareImageView, reqWidth : Int, reqHeight : Int){
        imageView.tag = path
        val bitmap : Bitmap? = getBitmapFromMemoryCache(path)
        if(bitmap != null){
            imageView.setImageBitmap(bitmap)
            return
        }
        val loadBitmapTask = Runnable {
            val bitmap = Utils.decodeSampledBitmapFromFile(mContext.resources, path, reqWidth, reqHeight)
            addBitmapToMemoryCache(path, bitmap)
            val result = BitmapBean(imageView, bitmap, path)
            mHandler.obtainMessage(0, result).sendToTarget()
        }
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask)
    }

    private fun getDiskCacheDir(context: Context, uniqueName : String) : File{
        val externaStorageAvailable = Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)
        val cachePath : String
        cachePath = if(externaStorageAvailable){
            context.externalCacheDir.path
        }else{
            context.cacheDir.path
        }
        return File(cachePath+File.separator+uniqueName)
    }

    private fun getUsableSpace(path : File) : Long{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return path.usableSpace
        }
        val statFs = StatFs(path.path)
        return statFs.blockSize.toLong() * statFs.availableBlocks.toLong()
    }

    companion object {

        private var ourInstance: ImageLoader? = null

        fun build(context: Context): ImageLoader? {
            if(ourInstance == null) {
                synchronized(ImageLoader::class.java){
                    if(ourInstance == null){
                        ourInstance = ImageLoader(context)
                    }
                }
            }
            return ourInstance
        }
    }
}
