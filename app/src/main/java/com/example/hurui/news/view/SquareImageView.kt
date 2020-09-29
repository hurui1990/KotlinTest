package com.example.hurui.news.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.example.hurui.news.R
import com.example.hurui.news.utils.Utils

/**
 * Created by hurui on 2018/3/18.
 */
class SquareImageView : AppCompatImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec), getDefaultSize(0,heightMeasureSpec))
        var childWidthSize = measuredWidth
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,MeasureSpec.EXACTLY)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}