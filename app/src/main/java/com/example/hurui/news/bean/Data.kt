package com.example.hurui.news.bean

/**
 * Created by hurui on 2017/5/25.
 */
data class NewType(var name:String, var type:String)

data class NewsDetail(
    val title : String, /*标题*/
    val date : String, /*时间*/
    val author_name : String, /*作者*/
    val thumbnail_pic_s : String, /*图片1*/
    val thumbnail_pic_s02 : String, /*图片2*/
    val thumbnail_pic_s03 : String, /*图片3*/
    val url : String, /*新闻链接*/
    val uniquekey : String, /*唯一标识*/
    val type : String, /*类型一*/
    val realtype : String/*类型二*/
)

data class Data(
        val stat:String,
        val data:ArrayList<NewsDetail>
)

data class Result(
        val reason:String,
        val result:Data
)

