package com.example.hurui.news.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.example.hurui.news.R
import com.example.hurui.news.utils.Utils

/**
 * Created by hurui on 2018/3/18.
 */
class SquareImageView : ImageView {

    var mWidth : Int? = null
    var imageSize : Int? = null

    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mWidth = Utils.getScreenWidth(context)
        var divide = context.resources.getDimension(R.dimen.image_view_divide).toInt()
        imageSize  = (mWidth!! - divide * 3) / 4
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //将ImageView设置为正方形
        setMeasuredDimension(imageSize!!, imageSize!!)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setImageDrawable(null)
    }
}