package com.example.hurui.news.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.hurui.news.R
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.main_toolbar.*
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*


/**
 * Created by hurui on 2017/8/2.
 */
class MapActivity : AppCompatActivity(){

    var aMap : AMap? = null
    var mMyLocationStyle : MyLocationStyle? = null

    var mLocationListener : AMapLocationListener? = null
    var mLocationOption: AMapLocationClientOption? = null
    var mLocationClient : AMapLocationClient? = null
    var mMapLoaction : AMapLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        toolbar.title = "地图"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView!!.onCreate(savedInstanceState)
    }

    fun initMap(){
        if(aMap == null) {
            aMap = mMapView.map
        }
        aMap!!.isTrafficEnabled = true
        aMap!!.mapType = AMap.MAP_TYPE_NORMAL

        mMyLocationStyle = MyLocationStyle()
        mMyLocationStyle!!.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on))
        mMyLocationStyle!!.strokeColor(Color.argb(0, 0, 0, 0))// 设置圆形的边框颜色
        mMyLocationStyle!!.radiusFillColor(Color.argb(0, 0, 0, 0))// 设置圆形的填充颜色
        mMyLocationStyle!!.strokeWidth(0f)// 设置圆形的边框粗细
        mMyLocationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        aMap!!.myLocationStyle = mMyLocationStyle
        aMap!!.isMyLocationEnabled = true
        var latlonPosition : LatLng = LatLng(mMapLoaction!!.latitude,mMapLoaction!!.longitude)
        aMap!!.moveCamera(CameraUpdateFactory.changeLatLng(latlonPosition))

    }

    fun setLocationOption(){
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationListener = AMapLocationListener { aMapLocation -> run{
            Log.i("MapActivity", "返回值："+aMapLocation.city)
            mMapLoaction = aMapLocation
            initMap()
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

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume()
        setLocationOption()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        finish()
        overridePendingTransition(R.anim.activity_enter_after,R.anim.activity_out_after)
        return super.onKeyDown(keyCode, event)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.activity_enter_after,R.anim.activity_out_after)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}