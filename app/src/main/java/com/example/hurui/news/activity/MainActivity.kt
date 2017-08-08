package com.example.hurui.news.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.example.hurui.news.R
import com.example.hurui.news.adapter.DrawerListAdapter
import com.example.hurui.news.presenter.LoadNewsPresenter
import com.example.hurui.news.utils.Utils
import com.example.hurui.news.view.LoadNewsView
import kotlinx.android.synthetic.main.drawerlayout.*
import kotlinx.android.synthetic.main.main_toolbar.*
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.hurui.news.adapter.ViewpagerAdapter
import com.example.hurui.news.bean.*
import kotlinx.android.synthetic.main.drawerfooter.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() ,LoadNewsView, DrawerListAdapter.OnItemClickListener, ViewPager.OnPageChangeListener {

    var newTypes : ArrayList<NewType>? = null
    var drawerItemTypes : ArrayList<MenuType>? =null

    var drawerAdapter : DrawerListAdapter? = null
    var drawerTogger : ActionBarDrawerToggle? = null
    var mLocationListener : AMapLocationListener? = null
    var mLocationOption: AMapLocationClientOption? = null
    var mLocationClient : AMapLocationClient? = null
    var sharePre : SharedPreferences? = null
    var editor : SharedPreferences.Editor? = null
    var cityname : String? = null
    var mMapLoaction : AMapLocation? = null
    var mLoadNewsPresenter : LoadNewsPresenter? = null
    var fragmentList : ArrayList<Fragment>? = null
    var screenWidth : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawerlayout)

        var wm : WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager;

        screenWidth = wm.defaultDisplay.width

        toolbar.title = "新闻速递"
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
        initFragments()
        setLocationOption()
    }

    fun setLocationOption(){
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationListener = AMapLocationListener { aMapLocation -> run{
            Log.i("======", "返回值："+aMapLocation.city)
            mMapLoaction = aMapLocation
            if(aMapLocation.city!!.length > 0){
                cityname = aMapLocation.city
                city_name.text = cityname
            }
            editor = sharePre!!.edit()
            editor!!.putString("cityname", cityname).commit()
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

    fun initFragments(){
        fragmentList = ArrayList<Fragment>()
        for(i in 0..9){
            var fragment : Fragment = NewsFragment()
            val bundle = Bundle()
            bundle.putString("type", newTypes!![i].type)
            fragment.arguments = bundle
            fragmentList!!.add(fragment)
        }
        view_pager.adapter = ViewpagerAdapter(supportFragmentManager, fragmentList!!)
        view_pager.setOnPageChangeListener(this)
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

        screenWidth = Utils.getScreenWidth(this)

        for (i in newTypes!!.indices){
            val newType : NewType = newTypes!![i]
            val textView : TextView = TextView(this)
            textView.text = newType.name
            textView.gravity = Gravity.CENTER
            textView.textSize = resources.getDimension(R.dimen.title_text_size)
            textView.width = screenWidth!! / 7
            textView.setTextColor(resources.getColor(R.color.text_color))
            if (i == 0) {
                textView.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                mLoadNewsPresenter!!.loadNews(newType.type)
            }
            textView.setOnClickListener({OnClickNewType(newType,i)})
            title_view.addView(textView)
        }
    }

    fun OnClickNewType(oldType: NewType, i: Int){
        view_pager.setCurrentItem(i,true)
        for (i in newTypes!!.indices){
            val newType = newTypes!![i]
            if(newType.type == oldType.type){
                (title_view.getChildAt(i) as TextView).setTextColor(resources.getColor(R.color.colorPrimaryDark))
            }else{
                (title_view.getChildAt(i) as TextView).setTextColor(resources.getColor(R.color.text_color))
            }
        }
    }

    override fun setLoadNews(result: ArrayList<NewsDetail>) {}

    override fun loadNewsError(errorType: Int) {}

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

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        OnClickNewType(newTypes!![position], position)
        title_scroll_view.scrollTo((screenWidth!! / 7) * position, 0)
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
        drawerItemTypes!!.add(MenuType(R.drawable.ic_user, "我的博客"))
        drawerItemTypes!!.add(MenuType(R.drawable.ic_settings, "设置"))

        drawerAdapter = DrawerListAdapter(this, drawerItemTypes!!)
        drawer_menu.layoutManager = LinearLayoutManager(this)
        drawer_menu.adapter = drawerAdapter
        drawerAdapter!!.notifyDataSetChanged()

        drawerAdapter!!.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View, position: Int) {
        var menuType : MenuType = drawerItemTypes!![position]
        when(menuType.itemtext){
            "新闻咨询" -> { drawerlayout.closeDrawer(Gravity.START) }
            "本地图片" -> {
                //TODO: 跳转到本地图片页面
            }
            "地图" -> {
                var intent : Intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_out)
            }
            "我的博客" -> {
                //TODO: 跳转到关于我页面
                var intent : Intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_out)
            }
            "设置" -> {
                //TODO: 跳转到设置页面
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        drawerlayout.closeDrawer(Gravity.START)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient!!.stopLocation()
        mLocationClient!!.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var build = AlertDialog.Builder(this)
        build.setTitle("提示")
        build.setMessage("客官不再耍一会儿？")
        build.setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which -> run{
            finish()
        } })
        build.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which -> run{

        } })
        build.create().show()
        return super.onKeyDown(keyCode, event)
    }

    //权限管理
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            20 -> {
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。

                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    Toast.makeText(this,"你拒绝了权限，不能使用该应用",Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}