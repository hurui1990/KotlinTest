package com.example.hurui.news.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by Administrator on 2015/9/11.
 */
class RoundImageView : ImageView {
    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onDraw(canvas: Canvas) {

        val drawable = drawable ?: return
        if (width == 0 || height == 0) {
            return
        }
        val b = (drawable as BitmapDrawable).bitmap ?: return
        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
        val w = width
        val h = height
        val roundBitmap = getCroppedBitmap(bitmap, w)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)

    }

    companion object {

        fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap {
            val sbmp: Bitmap
            if (bmp.width != radius || bmp.height != radius)
                sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false)
            else
                sbmp = bmp
            val output = Bitmap.createBitmap(sbmp.width,
                    sbmp.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val color = 0xffa19774.toInt()
            val paint = Paint()
            val rect = Rect(0, 0, sbmp.width, sbmp.height)

            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.parseColor("#BAB399")
            canvas.drawCircle(sbmp.width / 2 + 0.7f, sbmp.height / 2 + 0.7f,
                    sbmp.width / 2 + 0.1f, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(sbmp, rect, rect, paint)
            return output
        }
    }
}
