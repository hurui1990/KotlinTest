package com.example.hurui.news.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.SearchView
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.hurui.news.R
import kotlinx.android.synthetic.main.activity_map.*
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.*
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.example.hurui.news.adapter.PoisAdapter
import com.example.hurui.news.view.MyDivider
import java.util.*

/**
 * Created by hurui on 2017/8/2.
 */
class MapActivity : AppCompatActivity(),
        View.OnClickListener,
        SearchView.OnQueryTextListener,
        PoiSearch.OnPoiSearchListener, SearchView.OnCloseListener, PoisAdapter.OnItemClickListener {

    var aMap: AMap? = null
    var mMyLocationStyle: MyLocationStyle? = null

    var mLocationListener: AMapLocationListener? = null
    var mLocationOption: AMapLocationClientOption? = null
    var mLocationClient: AMapLocationClient? = null
    var mMapLoaction: AMapLocation? = null
    var mUiSettings: UiSettings? = null
    var mQuery : PoiSearch.Query? = null
    var poiSearch : PoiSearch? = null
    var mPoiAdapter : PoisAdapter? = null
    var orignZoomLevel : Float = 17f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView!!.onCreate(savedInstanceState)


        if (aMap == null) {
            aMap = mMapView.map
            mUiSettings = aMap!!.uiSettings
            mUiSettings
        }

        setLocationOption()

        mUiSettings!!.isZoomControlsEnabled = false
        aMap!!.mapType = AMap.MAP_TYPE_NORMAL

        mPoiAdapter = PoisAdapter(this)
        search_list.layoutManager = LinearLayoutManager(this)
        search_list.addItemDecoration(MyDivider(this, 1))
        search_list.adapter = mPoiAdapter

        mPoiAdapter!!.setOnItemClickListener(this)

        open_map_setting.setOnClickListener(this)
        searchview.setOnQueryTextListener(this)
        searchview.setOnCloseListener(this)
        maxZoom.setOnClickListener(this)
        minZoom.setOnClickListener(this)
        location_btn.setOnClickListener(this)
    }

    fun setListViewGone(){
        var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f)
        scaleAnim.duration = 300
        search_list.animation = scaleAnim
        search_list.visibility = View.GONE
    }

    override fun onItemClick(view: View, poiitem: PoiItem) {
        var poi : PoiItem = poiitem
        Toast.makeText(this,poi.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onClose(): Boolean {
        setListViewGone()
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!!.length <= 0){
            setListViewGone()
        }else {
            if(search_list.visibility != View.VISIBLE){
                var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f)
                scaleAnim.duration = 300
                search_list.animation = scaleAnim
                search_list.visibility = View.VISIBLE
            }
        }
        mQuery = PoiSearch.Query(query, "", mMapLoaction!!.city)
        poiSearch = PoiSearch(this, mQuery)
        poiSearch!!.setOnPoiSearchListener(this)
        poiSearch!!.searchPOIAsyn()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
    }

    override fun onPoiSearched(result: PoiResult?, p1: Int) {
        Log.i("==============", result!!.pois.size.toString())
        if(result!!.pois.size == 0){
            var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f)
            scaleAnim.duration = 300
            layout_no_result.animation = scaleAnim
            layout_no_result.visibility = View.VISIBLE
            Timer().schedule(object : TimerTask() {
                override fun run() {

                    runOnUiThread {
                        var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f)
                        scaleAnim.duration = 300
                        layout_no_result.animation = scaleAnim
                        layout_no_result.visibility = View.GONE
                    }
                }
            }, 3000)
        }
        mPoiAdapter!!.setData(result!!.pois)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.open_map_setting -> {
                map_drawer.openDrawer(Gravity.END)
            }
            R.id.maxZoom -> {
                if(orignZoomLevel < 18f){
                    orignZoomLevel++
                }
                aMap!!.moveCamera(CameraUpdateFactory.zoomTo(orignZoomLevel))
            }
            R.id.minZoom -> {
                if(orignZoomLevel > 1){
                    orignZoomLevel--
                }
                aMap!!.moveCamera(CameraUpdateFactory.zoomTo(orignZoomLevel))
            }
            R.id.location_btn -> {
                setLocationOption()
            }
        }
    }

    fun initMap() {
        mMyLocationStyle = MyLocationStyle()
        mMyLocationStyle!!.strokeColor(resources.getColor(R.color.white))// 设置圆形的边框颜色
        mMyLocationStyle!!.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location))
        mMyLocationStyle!!.radiusFillColor(resources.getColor(R.color.maolocation))// 设置圆形的填充颜色
        mMyLocationStyle!!.strokeWidth(1f)// 设置圆形的边框粗细
        mMyLocationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        aMap!!.myLocationStyle = mMyLocationStyle
        aMap!!.isMyLocationEnabled = true
        aMap!!.showIndoorMap(true)

        var latlonPosition: LatLng = LatLng(mMapLoaction!!.latitude, mMapLoaction!!.longitude)
        aMap!!.moveCamera(CameraUpdateFactory.changeLatLng(latlonPosition))
        aMap!!.moveCamera(CameraUpdateFactory.zoomTo(orignZoomLevel))
    }

    fun setLocationOption() {
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationListener = AMapLocationListener { aMapLocation ->
            run {
                Snackbar.make(map_drawer,"您当前的位置:"+aMapLocation.address,Snackbar.LENGTH_LONG).show()
                Log.i("MapActivity", "返回值：" + aMapLocation.city)
                mMapLoaction = aMapLocation
                initMap()
            }
        }
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

    //切换地图显示模式
    fun setMapDisplayType(type : Int){
        when(type){
            AMap.MAP_TYPE_NORMAL -> {
                //TODO 普通模式
                aMap!!.mapType = AMap.MAP_TYPE_NORMAL
                mMapView.onResume()
            }
            AMap.MAP_TYPE_SATELLITE -> {
                //TODO 卫星地图
                aMap!!.mapType = AMap.MAP_TYPE_SATELLITE
                mMapView.onResume()
            }
            AMap.MAP_TYPE_NIGHT -> {
                //TODO 夜间地图
                aMap!!.mapType = AMap.MAP_TYPE_NIGHT
                mMapView.onResume()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume()
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
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            overridePendingTransition(R.anim.activity_enter_after,R.anim.activity_out_after)
        }
        return super.onKeyDown(keyCode, event)
    }

}