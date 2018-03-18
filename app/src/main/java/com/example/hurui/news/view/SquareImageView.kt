package com.example.hurui.news.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by hurui on 2018/3/18.
 */
class SquareImageView : ImageView {

    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setImageDrawable(null)
    }
}