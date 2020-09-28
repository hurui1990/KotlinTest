package com.example.hurui.news.activity

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.hurui.news.R
import com.example.hurui.news.adapter.ViewpagerAdapter
import com.example.hurui.news.base.BaseActivity
import com.example.hurui.news.bean.HeWeather5Bean
import com.example.hurui.news.bean.NewType
import com.example.hurui.news.bean.WeatherData
import com.example.hurui.news.fragment.NewsFragment
import com.example.hurui.news.presenter.LoadNewsPresenter
import com.example.hurui.news.view.LoadNewsView
import kotlinx.android.synthetic.main.drawerfooter.*
import kotlinx.android.synthetic.main.drawerlayout.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : BaseActivity() ,LoadNewsView, NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "MainActivity"
    private lateinit var newTypes : ArrayList<NewType>
    private lateinit var drawerTogger : ActionBarDrawerToggle
    private lateinit var mLocationListener : AMapLocationListener
    private val mLocationOption by lazy { AMapLocationClientOption() }
    private val mLocationClient by lazy { AMapLocationClient(applicationContext) }
    private lateinit var cityName : String
    private lateinit var mMapLocation : AMapLocation
    private lateinit var mLoadNewsPresenter : LoadNewsPresenter
    private lateinit var fragmentList : ArrayList<Fragment>
    private lateinit var viewpagerAdapter : ViewpagerAdapter
    private lateinit var dialog : Dialog

    override fun getLayoutId(): Int {
        return R.layout.drawerlayout
    }

    override fun initView() {
        super.initView()
        toolbar.title = "新闻速递"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_open)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        drawerTogger = ActionBarDrawerToggle(this, drawerlayout, toolbar ,R.string.back, R.string.main_title)
        drawerlayout.setDrawerListener(drawerTogger)
        mLoadNewsPresenter = LoadNewsPresenter(this)

        navigation_view.setNavigationItemSelectedListener(this)
        navigation_view.itemIconTintList = null

        initScrollView()
        initFragments()
        setLocationOption()
    }

    private fun setLocationOption(){
        mLocationListener = AMapLocationListener { aMapLocation -> run{
            Log.i(TAG, "返回值："+aMapLocation.city)
            mMapLocation = aMapLocation
            if(aMapLocation.city!!.isNotEmpty()){
                cityName = aMapLocation.city
                city_name.text = cityName
            }
            mLoadNewsPresenter!!.loadWeather(cityName!!)
        } }
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.isOnceLocationLatest = true
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true
        //设置超时事件
        mLocationOption.httpTimeOut = 5000
        //设置定义的属性
        mLocationClient.setLocationOption(mLocationOption)
        //设置监听事件
        mLocationClient.setLocationListener(mLocationListener)
        //开始定位
        mLocationClient.startLocation()
    }

    private fun initFragments(){
        fragmentList = ArrayList()
        for(i in 0 until newTypes.size){
            val fragment = NewsFragment()
            val bundle = Bundle()
            bundle.putString("type", newTypes[i].type)
            fragment.arguments = bundle
            fragmentList.add(fragment)
        }

        viewpagerAdapter =  ViewpagerAdapter(supportFragmentManager, fragmentList, newTypes)
        view_pager.adapter = viewpagerAdapter
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.setTabsFromPagerAdapter(viewpagerAdapter)
    }

    //动态添加头部的新闻类型标签的布局
    private fun initScrollView(){
        newTypes = ArrayList()
        newTypes.add(NewType("头条","top"))
        newTypes.add(NewType("社会","shehui"))
        newTypes.add(NewType("国内","guonei"))
        newTypes.add(NewType("国际","guoji"))
        newTypes.add(NewType("娱乐","yule"))
        newTypes.add(NewType("体育","tiyu"))
        newTypes.add(NewType("军事","junshi"))
        newTypes.add(NewType("科技","keji"))
        newTypes.add(NewType("财经","caijing"))
        newTypes.add(NewType("时尚","shishang"))

        for (i in newTypes.indices){
            tab_layout.addTab(tab_layout.newTab().setText(newTypes[i].name))
        }
    }

    override fun setLoadNews(result: String) {}

    override fun loadNewsError(errorType: Int) {}

    override fun loadWeather(result: WeatherData) {
        val nowWeather : HeWeather5Bean = result.HeWeather5[0]
        if(nowWeather.status == "unknown city"){
            return
        }
        Log.i(TAG, nowWeather.now.toString())
        now_temp.text = nowWeather.now.tmp + "°C"
        now_weather.text = nowWeather.now.cond.txt
    }

    override fun loadWeatherError(errorType: Int) {}

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerTogger.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_news -> {
                drawerlayout.closeDrawer(GravityCompat.START)
            }
            R.id.menu_localpicture -> {
                //TODO: 跳转到本地图片页面
                val intent = Intent(this, MediasActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_out)
            }
            R.id.menu_map -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_out)
            }
            R.id.menu_blog -> {
                //TODO: 跳转到关于我页面
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_out)
            }
            R.id.menu_setting -> {
                //TODO: 跳转到设置页面
            }
        }
        return true
    }

    override fun onStop() {
        drawerlayout.closeDrawer(GravityCompat.START)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient.stopLocation()
        mLocationClient.onDestroy()
        drawerlayout.setDrawerListener(null)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            dialog = AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("客官不再耍一会儿？")
                    .setPositiveButton("确定") { _, _ ->
                        run {
                            finish()
                        }
                    }
                    .setNegativeButton("取消") { _, _ ->
                        run {
                            dialog.dismiss()
                        }
                    }.create()
            dialog.show()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    //权限管理
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            20 -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
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