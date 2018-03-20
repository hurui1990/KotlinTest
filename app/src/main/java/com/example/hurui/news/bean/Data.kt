package com.example.hurui.news.bean

import android.graphics.Bitmap
import com.example.hurui.news.view.SquareImageView

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

data class MenuType(var image:Int, var itemtext:String)

data class WeatherData(
        val HeWeather5:ArrayList<HeWeather5Bean>
)

data class HeWeather5Bean(
        val aqi:AqiBean,
        val basic:BasicBean,
        val now:NowBean,
        val suggestion:SuggestionBean,
        val status:String,
        val daily_forecast:ArrayList<DailyForecastBean>,
        val hourly_forecast:ArrayList<HourlyForecastBean>
)

data class AqiBean(
        val city:CityBean
)

data class CityBean(
        val aqi:String,
        val co:String,
        val no2:String,
        val o3:String,
        val pm10:String,
        val pm25:String,
        val qlty:String,
        val so2:String
)

data class BasicBean(
        val city:String,
        val cnty:String,
        val id:String,
        val lat:String,
        val lon:String,
        val update:UpdateBean
)

data class UpdateBean(
        val loc:String,
        val utc:String
)

data class NowBean(
        val cond : CondBean,
        val fl : String,
        val hum : String,
        val pcpn : String,
        val pres : String,
        val tmp : String,
        val vis : String,
        val wind : WindBean
)

data class CondBean(
        val code : String,
        val txt : String

)

data class WindBean(
        val deg : String,
        val dir : String,
        val sc : String,
        val spd : String
)


data class SuggestionBean(
        val air:AirBean,
        val comf:ComfBean,
        val cw:CwBean,
        val drsg:DrsgBean,
        val flu:FluBean,
        val sport:SportBean,
        val trav:TravBean,
        val uv:UvBean
)

data class AirBean (
        val brf:String,
        val txt:String
)
data class ComfBean (
        val brf:String,
        val txt:String
)
data class CwBean (
        val brf:String,
        val txt:String
)
data class DrsgBean (
        val brf:String,
        val txt:String
)
data class FluBean (
        val brf:String,
        val txt:String
)
data class SportBean (
        val brf:String,
        val txt:String
)
data class TravBean (
        val brf:String,
        val txt:String
)
data class UvBean (
        val brf:String,
        val txt:String
)


data class DailyForecastBean(
        val astro:AstroBean,
        val cond:CondBeanX,
        val date:String,
        val hum:String,
        val pcpn:String,
        val pop:String,
        val pres:String,
        val tmp:TmpBean,
        val uv:String,
        val vis:String,
        val wind:WindBeanX
)

data class AstroBean(
        val mr:String,
        val ms:String,
        val sr:String,
        val ss:String
)
data class CondBeanX(
        val code_d:String,
        val code_n:String,
        val txt_d:String,
        val txt_n:String
)
data class TmpBean(
        val max: String,
        val min: String
)
data class WindBeanX(
        val deg: String,
        val dir: String,
        val sc: String,
        val spd: String
)

data class HourlyForecastBean(
        val cond: CondBeanXX,
        val date: String,
        val hum: String,
        val pop: String,
        val pres: String,
        val tmp: String,
        val wind: WindBeanXX
)

data class CondBeanXX(
        val code: String,
        val txt: String
)
data class WindBeanXX(
        val deg: String,
        val dir: String,
        val sc: String,
        val spd: String
)

data class MediaBean(
        val type: String,
        val path: String,
        val name: String,
        val size: String,
        val thumbPath: String,
        val duration: String,
        val title : String,
        val singer : String,
        val album : String
)

data class BitmapBean (
        val imageview : SquareImageView,
        val bitmap : Bitmap,
        val path : String
)