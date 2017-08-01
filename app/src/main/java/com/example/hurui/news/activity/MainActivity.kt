package com.example.hurui.news.activity

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.amap.api.location.AMapLocationClient
import com.example.hurui.news.R
import com.example.hurui.news.adapter.DrawerListAdapter
import com.example.hurui.news.adapter.RecyclerAdapter
import com.example.hurui.news.presenter.LoadNewsPresenter
import com.example.hurui.news.utils.Utils
import com.example.hurui.news.view.LoadNewsView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.drawerlayout.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.io.File
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.hurui.news.bean.*
import java.util.*

class MainActivity : AppCompatActivity() ,LoadNewsView{

    var newTypes : ArrayList<NewType>? = null
    var drawerItemTypes : ArrayList<MenuType>? =null
    var mLoadNewsPresenter : LoadNewsPresenter? = null

    var dataAdapter : RecyclerAdapter? = null
    var dataList : ArrayList<NewsDetail>? =null

    var drawerAdapter : DrawerListAdapter? = null
    var drawerTogger : ActionBarDrawerToggle? = null
    var mLocationListener : AMapLocationListener? = null
    var mLocationOption: AMapLocationClientOption? = null
    var mLocationClient : AMapLocationClient? = null
    var sharePre : SharedPreferences? = null
    var editor : SharedPreferences.Editor? = null
    var cityname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "新闻世界"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_open)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        drawerTogger = ActionBarDrawerToggle(this, drawerlayout, toolbar ,R.string.back, R.string.main_title)
        drawerlayout.setDrawerListener(drawerTogger)

        mLoadNewsPresenter = LoadNewsPresenter(this)

        initScrollView()
        initDrawerMenu()

        dataList = ArrayList<NewsDetail>()
        dataAdapter = RecyclerAdapter(this)
        dataAdapter!!.setData(dataList!!)
        recycler_content.layoutManager = LinearLayoutManager(this)
        recycler_content.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recycler_content.adapter = dataAdapter
        OnClickNewType(NewType("头条","top"))

        setLocationOption()
    }

    fun setLocationOption(){
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationListener = AMapLocationListener { aMapLocation -> run{
            Log.i("======", "返回值："+aMapLocation.city)
            if(aMapLocation.city!!.length > 0){
                cityname = aMapLocation.city
                city_name.text = cityname
            }
            editor = sharePre!!.edit()
            editor!!.putString("cityname", cityname)
            mLoadNewsPresenter!!.loadWeather(cityname!!)
        } }
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption!!.isOnceLocationLatest = true
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption!!.isNeedAddress = true
        //设置超时事件
        mLocationOption!!.httpTimeOut = 2000
        //设置定义的属性
        mLocationClient!!.setLocationOption(mLocationOption)
        //设置监听事件
        mLocationClient!!.setLocationListener(mLocationListener)
        //开始定位
        mLocationClient!!.startLocation()
    }

    //动态添加头部的新闻类型标签的布局
    fun initScrollView(){
        newTypes = ArrayList<NewType>()
        newTypes!!.add(NewType("头条","top"))
        newTypes!!.add(NewType("社会","shehui"))
        newTypes!!.add(NewType("国内","guonei"))
        newTypes!!.add(NewType("国际","guoji"))
        newTypes!!.add(NewType("娱乐","yule"))
        newTypes!!.add(NewType("体育","tiyu"))
        newTypes!!.add(NewType("军事","junshi"))
        newTypes!!.add(NewType("科技","keji"))
        newTypes!!.add(NewType("财经","caijing"))
        newTypes!!.add(NewType("时尚","shishang"))

        val screenWidth : Int = Utils.getScreenWidth(this)

        for (i in newTypes!!.indices){
            val newType : NewType = newTypes!![i]
            val textView : TextView = TextView(this)
            textView.text = newType.name
            textView.gravity = Gravity.START
            textView.textSize = resources.getDimension(R.dimen.title_text_size)
            textView.width = screenWidth / 7
            textView.setTextColor(resources.getColor(R.color.text_color))
            if (i == 0) {
                textView.setTextColor(resources.getColor(R.color.select_text_color))
                mLoadNewsPresenter!!.loadNews(newType.type)
            }
            textView.setOnClickListener({OnClickNewType(newType)})
            title_view.addView(textView)
        }
    }

    fun OnClickNewType(oldType: NewType){
        for (i in newTypes!!.indices){
            val newType = newTypes!![i]
            if(newType.type == oldType.type){
                (title_view.getChildAt(i) as TextView).setTextColor(resources.getColor(R.color.select_text_color))
            }else{
                (title_view.getChildAt(i) as TextView).setTextColor(resources.getColor(R.color.text_color))
            }
        }
        mLoadNewsPresenter!!.loadNews(oldType.type)
    }

    override fun setLoadNews(result: ArrayList<NewsDetail>) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),20)
        }else{
            // 有权限
            setData()
        }

    }

    //设置RecyclerView的数据
    fun setData(){
        var gson : Gson = Gson()
        var resultStr : Result = gson.fromJson(getDataFromLocal(), Result::class.java)
        //成功获取到新闻信息
        dataAdapter?.setData(resultStr.result.data)
    }

    //获取模拟数据
    fun getDataFromLocal() : String{
        var path = Environment.getExternalStorageDirectory().toString()+"/data.json"
        var file = File(path)
        var line = file.readText()
        return line
    }

    override fun loadNewsError(errorType: Int) {
        //获取新闻信息失败
        Toast.makeText(this,errorType.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun loadWeather(result: WeatherData) {
        var nowWeather : HeWeather5Bean = result.HeWeather5[0]
        Log.i("天气情况:","天气情况:"+ nowWeather.status)
        Log.i("天气情况:","天气情况:"+ nowWeather.aqi)
        Log.i("天气情况:","天气情况:"+ nowWeather.basic)
        Log.i("天气情况:","天气情况:"+ nowWeather.now)
        Log.i("天气情况:","天气情况:"+ nowWeather.suggestion)
        Log.i("天气情况:","天气情况:"+ nowWeather.daily_forecast)
        Log.i("天气情况:","天气情况:"+ nowWeather.hourly_forecast)
        if(nowWeather.status == "unknown city"){
            return
        }
        now_temp.text = nowWeather!!.now!!.tmp!! + "°C"
        now_weather.text = nowWeather!!.now!!.cond!!.txt!!
    }

    override fun loadWeatherError(errorType: Int) {

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerTogger!!.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initDrawerMenu(){
        sharePre = getSharedPreferences("city",Context.MODE_PRIVATE)
        cityname = sharePre!!.getString("cityname", "成都市")
        Log.i("=========","初始值："+cityname)
        city_name.text = cityname

        drawerItemTypes = ArrayList<MenuType>()
        drawerItemTypes!!.add(MenuType(R.drawable.ic_news, "新闻咨询"))
        drawerItemTypes!!.add(MenuType(R.drawable.ic_picture, "本地图片"))
        drawerItemTypes!!.add(MenuType(R.drawable.ic_map, "地图"))
        drawerItemTypes!!.add(MenuType(R.drawable.ic_user, "关于我"))
        drawerItemTypes!!.add(MenuType(R.drawable.ic_settings, "设置"))

        drawerAdapter = DrawerListAdapter(this, drawerItemTypes!!)
        drawer_menu.layoutManager = LinearLayoutManager(this)
        drawer_menu.adapter = drawerAdapter
        drawerAdapter!!.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient!!.stopLocation()
        mLocationClient!!.onDestroy()
    }

    //权限管理
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            20 -> {
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    setData()
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    Toast.makeText(this,"你拒绝了权限，不能使用该应用",Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}
