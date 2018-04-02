package com.example.hurui.news.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.*
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.amap.api.services.route.*
import com.example.hurui.news.R
import com.example.hurui.news.adapter.PoisAdapter
import com.example.hurui.news.view.MyDivider
import com.suke.widget.SwitchButton
import kotlinx.android.synthetic.main.activity_map.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by hurui on 2017/8/2.
 */
class MapActivity : AppCompatActivity(),
        View.OnClickListener,
        SearchView.OnQueryTextListener,
        PoiSearch.OnPoiSearchListener, SearchView.OnCloseListener, PoisAdapter.OnItemClickListener, RadioGroup.OnCheckedChangeListener, SwitchButton.OnCheckedChangeListener, AMap.OnCameraChangeListener, RouteSearch.OnRouteSearchListener, TextWatcher {

    private var aMap: AMap? = null
    private var mMyLocationStyle: MyLocationStyle? = null

    private var mLocationListener: AMapLocationListener? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mMapLoaction: AMapLocation? = null
    private var mUiSettings: UiSettings? = null
    private var mQuery : PoiSearch.Query? = null
    private var poiSearch : PoiSearch? = null
    private var mPoiAdapter : PoisAdapter? = null
    private var orignZoomLevel : Float = 12f
    private var mTrafficPrefre : SharedPreferences? = null
    private var mIndoorPrefre : SharedPreferences? = null
    private var mReliPrefre : SharedPreferences? = null
    private var isTrafficOpen : Boolean = false
    private var isIndoorOpen : Boolean = false
    private var isReliOpen : Boolean = false
    private var centerLatlon : LatLng? = null
    var isInDaohang : Boolean = false
    private val BUXING : Int = 0
    private val DRIVE : Int = 1
    private val BUS : Int = 2
    private val BIKE : Int = 3
    private var out_style : Int = 0
    private var oriText : String? = null
    private var desText : String? = null
    private var oriLatLonPoint : LatLonPoint? = null
    private var desLatLonPoint : LatLonPoint? = null
    private var routeSearch : RouteSearch? = null
    private var fromandto : RouteSearch.FromAndTo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView!!.onCreate(savedInstanceState)

        mTrafficPrefre = getSharedPreferences("traffic",Context.MODE_PRIVATE)
        mIndoorPrefre = getSharedPreferences("indoor",Context.MODE_PRIVATE)
        mReliPrefre = getSharedPreferences("reli",Context.MODE_PRIVATE)
        isTrafficOpen = mTrafficPrefre!!.getBoolean("isOpen", false)
        isIndoorOpen = mIndoorPrefre!!.getBoolean("isOpen", false)
        isReliOpen = mReliPrefre!!.getBoolean("isOpen", false)

        if (aMap == null) {
            aMap = mMapView.map
            mUiSettings = aMap!!.uiSettings
            mUiSettings
        }

        setLocationOption()

        mUiSettings!!.isZoomControlsEnabled = false
        aMap!!.mapType = AMap.MAP_TYPE_NORMAL
        aMap!!.isTrafficEnabled = isTrafficOpen
        aMap!!.showIndoorMap(isIndoorOpen)
        aMap!!.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(104.06, 30.67)))

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
        daohang.setOnClickListener(this)
        mMapView.setOnClickListener(this)
        img_switch_location.setOnClickListener(this)
        txt_daohang.setOnClickListener(this)
        mapType_seclect.setOnCheckedChangeListener(this)
        mapType_seclect.check(R.id.normal_map)
        out_select.setOnCheckedChangeListener(this)
        out_select.check(R.id.rb_buxing)
        traffic_switch_btn.setOnCheckedChangeListener(this)
        traffic_switch_btn.isChecked = isTrafficOpen
        indoor_switch_btn.setOnCheckedChangeListener(this)
        indoor_switch_btn.isChecked = isIndoorOpen
        reli_switch_btn.setOnCheckedChangeListener(this)
        reli_switch_btn.isChecked = isReliOpen

        et_orin.addTextChangedListener(this)
        et_des.addTextChangedListener(this)

    }

    override fun onItemClick(view: View, poiitem: PoiItem) {
        if(isInDaohang){
            if(et_orin.isFocused){
                oriLatLonPoint = poiitem.latLonPoint
                et_orin.setText(poiitem.toString(),TextView.BufferType.EDITABLE)
            }else if(et_des.isFocused){
                desLatLonPoint = poiitem.latLonPoint
                et_des.setText(poiitem.toString(),TextView.BufferType.EDITABLE)
            }
        }
        search_list.visibility = View.GONE
    }

    override fun onClose(): Boolean {
        setListViewGone()
        return false
    }

    //searchview输入事件监听
    override fun onQueryTextSubmit(query: String?): Boolean {
        var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f)
        scaleAnim.duration = 300
        search_list.animation = scaleAnim
        search_list.visibility = View.VISIBLE
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

    //POI搜索结果返回
    override fun onPoiSearched(result: PoiResult?, p1: Int) {
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

    //控件点击事件
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
            R.id.daohang -> {
                if(isInDaohang) {
                    daohang.setImageResource(R.drawable.ic_daohang)
                    isInDaohang = false
                    var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f)
                    scaleAnim.duration = 500
                    daohanglayout.animation = scaleAnim
                    daohanglayout.visibility = View.GONE

                    Timer().schedule(object : TimerTask() {
                        override fun run() {

                            runOnUiThread {
                                var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f)
                                scaleAnim.duration = 300
                                mapsearchlayout.animation = scaleAnim
                                mapsearchlayout.visibility = View.VISIBLE
                            }
                        }
                    }, 500)

                    var params : RelativeLayout.LayoutParams = search_list.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.BELOW, R.id.mapsearchlayout)
                    search_list.layoutParams = params
                }else{
                    daohang.setImageResource(R.drawable.ic_cancel)
                    isInDaohang = true
                    var scaleAnim: ScaleAnimation = ScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f)
                    scaleAnim.duration = 300
                    mapsearchlayout.animation = scaleAnim
                    mapsearchlayout.visibility = View.INVISIBLE

                    Timer().schedule(object : TimerTask() {
                        override fun run() {

                            runOnUiThread {
                                var scaleAnim: ScaleAnimation = ScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f)
                                scaleAnim.duration = 500
                                daohanglayout.animation = scaleAnim
                                daohanglayout.visibility = View.VISIBLE
                            }
                        }
                    }, 300)

                    var params : RelativeLayout.LayoutParams = search_list.layoutParams as RelativeLayout.LayoutParams
                    params.addRule(RelativeLayout.BELOW, R.id.daohanglayout)
                    search_list.layoutParams = params
                }

            }
            R.id.img_switch_location -> {
                var etChange : String = ""
                oriText = et_orin.text.toString()
                desText = et_des.text.toString()

                etChange = oriText!!
                oriText = desText
                desText = etChange

                et_orin.setText(oriText, TextView.BufferType.NORMAL)
                et_des.setText(desText, TextView.BufferType.NORMAL)

            }
            R.id.txt_daohang -> {
                routeSearch = RouteSearch(this)
                fromandto = RouteSearch.FromAndTo(oriLatLonPoint,desLatLonPoint)
                routeSearch!!.setRouteSearchListener(this)
                when(out_style){
                    BUXING -> { setBuxingPath() }
                    DRIVE -> { setDrivePath() }
                    BUS -> { setBusPath() }
                    BIKE -> { setBikePath() }
                }
            }
        }
    }

    fun setBuxingPath(){

    }

    fun setDrivePath(){}

    fun setBusPath(){}

    fun setBikePath(){}

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {

    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {

    }

    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

    }

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        Log.i("afterTextChanged", s!!.length.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mQuery = PoiSearch.Query(s.toString(), "", mMapLoaction!!.city)
        poiSearch = PoiSearch(this, mQuery)
        poiSearch!!.setOnPoiSearchListener(this)
        poiSearch!!.searchPOIAsyn()
    }

    //radiobutton点击事件
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.normal_map -> { setMapDisplayType(AMap.MAP_TYPE_NORMAL) }
            R.id.satellite_map -> { setMapDisplayType(AMap.MAP_TYPE_SATELLITE) }
            R.id.night_map -> { setMapDisplayType(AMap.MAP_TYPE_NIGHT) }
            R.id.rb_buxing -> {out_style = BUXING}
            R.id.rb_drive -> {out_style = DRIVE}
            R.id.rb_bike -> {out_style = BIKE}
            R.id.rb_bus -> {out_style = BUS}
        }
    }

    override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {

        when(view!!.id){
            R.id.traffic_switch_btn -> {
                var editor = mTrafficPrefre!!.edit()
                editor.putBoolean("isOpen", isChecked).commit()
                isTrafficOpen = isChecked
                when(isChecked){
                    true -> { aMap!!.isTrafficEnabled = true }
                    false -> { aMap!!.isTrafficEnabled = false }
                }
            }
            R.id.indoor_switch_btn -> {
                var editor = mIndoorPrefre!!.edit()
                editor.putBoolean("isOpen", isChecked).commit()
                isIndoorOpen = isChecked
                when(isChecked){
                    true -> { aMap!!.showIndoorMap(true) }
                    false -> { aMap!!.showIndoorMap(false) }
                }
            }
            R.id.reli_switch_btn -> {
                var editor = mReliPrefre!!.edit()
                editor.putBoolean("isOpen", isChecked).commit()
                isReliOpen = isChecked
                when(isChecked){
                    true -> {
                        setReliMap(centerLatlon!!.latitude,centerLatlon!!.longitude)
                    }
                    false -> {
                        aMap!!.clear(true)
                    }
                }
            }
        }
    }

    override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
        Log.i("==============",cameraPosition.toString())
        centerLatlon = LatLng(cameraPosition!!.target.latitude, cameraPosition.target.longitude)
        if(isReliOpen) {
            setReliMap(cameraPosition!!.target.latitude, cameraPosition.target.longitude)
        }
    }

    override fun onCameraChange(cameraPosition: CameraPosition?) {
    }

    fun setListViewGone(){
        var scaleAnim : ScaleAnimation = ScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f)
        scaleAnim.duration = 300
        search_list.animation = scaleAnim
        search_list.visibility = View.GONE
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

        var latlonPosition: LatLng = LatLng(mMapLoaction!!.latitude, mMapLoaction!!.longitude)
        aMap!!.moveCamera(CameraUpdateFactory.changeLatLng(latlonPosition))
        aMap!!.moveCamera(CameraUpdateFactory.zoomTo(orignZoomLevel))
        aMap!!.setOnCameraChangeListener(this)
    }

    fun setLocationOption() {
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationListener = AMapLocationListener { aMapLocation ->
            run {
                if(aMapLocation.address.isEmpty()){
                    Toast.makeText(this, "暂时无法定位，请稍后重试", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this, "您当前的位置:" + aMapLocation.address, Toast.LENGTH_LONG).show()
                }
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

    fun setReliMap(lat : Double, lon : Double){
        aMap!!.clear(true)
        var latlngs : ArrayList<LatLng> = ArrayList(5000)
        var x : Double = lat
        var y : Double = lon

        for (i in 0..4999) {
            var xX: Double = 0.0
            var yY: Double = 0.0
            xX = Math.random() * 0.5 - 0.25
            yY = Math.random() * 0.5 - 0.25
            latlngs.add(LatLng(x + xX, y + yY))
        }

        // 构建热力图 HeatmapTileProvider
        var builder : HeatmapTileProvider.Builder =  HeatmapTileProvider.Builder()
        builder.data(latlngs)
        // Gradient 的设置可见参考手册
        // 构造热力图对象
        var heatmapTileProvider : HeatmapTileProvider = builder.build()

        var tileOverlayOptions : TileOverlayOptions  = TileOverlayOptions()
        tileOverlayOptions.tileProvider(heatmapTileProvider) // 设置瓦片图层的提供者
        // 向地图上添加 TileOverlayOptions 类对象
        aMap!!.addTileOverlay(tileOverlayOptions)

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