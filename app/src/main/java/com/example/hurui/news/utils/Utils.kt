package com.example.hurui.news.utils

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Display
import android.view.WindowManager
import com.example.hurui.news.R
import java.io.FileNotFoundException

/**
 * Created by hurui on 2017/5/25.
 */
class Utils {

    companion object {

        /**
         * 获取屏幕的宽度
         * */
        fun getScreenWidth(context : Context): Int {
            val windowManager : WindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
            val display : Display = windowManager.defaultDisplay
            return display.width
        }

        /**
         * 将图片资源压缩并返回
         * */
        fun decodeSampledBitmapFromFile(resources: Resources, filePath : String, reqWidth : Int, reqHeight : Int) : Bitmap{
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath,options)

            options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565
            return try {
                BitmapFactory.decodeFile(filePath, options)
            }catch (e : FileNotFoundException){
                BitmapFactory.decodeResource(resources,R.drawable.ic_map)
            }

        }

        /**
         * 计算图片压缩的倍数
         * */
        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int) : Int{
            val width : Int = options.outWidth
            val height : Int = options.outHeight
            var inSampleSize = 1
            if (width > reqWidth || height > reqHeight){
                val halfWidth : Int = width / 2
                val halfHeight : Int = height / 2
                while ((halfWidth / inSampleSize) >= reqWidth
                        && (halfHeight / inSampleSize) >= reqHeight){
                    inSampleSize *= 4
                }
            }
            return inSampleSize
        }

        /**
         * 根据屏幕大小获取图片显示的大小
         * */
        fun calculateImageviewSize(context: Context) : Int{
            val screenWidth : Int = Utils.getScreenWidth(context)
            val divide = context.resources.getDimension(R.dimen.image_view_divide).toInt()
            return   (screenWidth - divide * 5) / 4
        }

        /**
         * 转换时长
         * */
        fun timeParse(duration: Long): String {
            var time = ""
            val minute = duration / 60000
            val seconds = duration % 60000
            val second = Math.round(seconds.toFloat() / 1000).toLong()
            if (minute < 10) {
                time += "0"
            }
            time += minute.toString() + ":"
            if (second < 10) {
                time += "0"
            }
            time += second
            return time
        }
    }
}