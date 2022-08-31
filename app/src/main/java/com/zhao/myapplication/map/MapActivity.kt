package com.zhao.myapplication.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMapOptions
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.zhao.myapplication.databinding.ActivityMapBinding


/**
 *
 */
class MapActivity : AppCompatActivity(), RouteSearch.OnRouteSearchListener {


    private lateinit var binding: ActivityMapBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initPermission()
    }

    private fun initPermission() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        }
    }


    private lateinit var mLocationClient: AMapLocationClient

    private val mLocationListener: AMapLocationListener = AMapLocationListener { mapLocation ->
        if (mapLocation.errorCode != 0) {
            Log.e(
                "mapError", "location Error, ErrCode:"
                        + mapLocation.errorCode + ", errInfo:"
                        + mapLocation.errorInfo
            )
            return@AMapLocationListener
        }
        binding.tvLocationTxt.text = mapLocation.toString()
        settingMapLocation(mapLocation)
    }

    private lateinit var mLocationOption: AMapLocationClientOption

    private fun initView() {
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
        binding.btStartLocation.setOnClickListener {
            if (getIsPermission()) {
                if (::mLocationClient.isInitialized) {
                    mLocationClient.startLocation()
                } else {
                    startLocation()
                }

            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
        binding.mapView.onCreate(null)


    }

    private fun getIsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocation() {
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationClient.setLocationListener(mLocationListener)
        mLocationOption = AMapLocationClientOption()
        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        //设置定位模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位结果获取
        //获取一次定位结果
        mLocationOption.isOnceLocation = true
        //获取3s内精度最高的一次
        mLocationOption.isOnceLocationLatest = true

        /* 设置是否返回地址信息（默认返回地址信息） */
        mLocationOption.isNeedAddress = true
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        //mLocationOption.setInterval(1000);

        //关闭缓存机制
        mLocationOption.isLocationCacheEnable = false

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.isMockEnable = true

        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.stopLocation()
        mLocationClient.startLocation()
    }

    /**
     * amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
    amapLocation.getLatitude();//获取纬度
    amapLocation.getLongitude();//获取经度
    amapLocation.getAccuracy();//获取精度信息
    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    amapLocation.getCountry();//国家信息
    amapLocation.getProvince();//省信息
    amapLocation.getCity();//城市信息
    amapLocation.getDistrict();//城区信息
    amapLocation.getStreet();//街道信息
    amapLocation.getStreetNum();//街道门牌号信息
    amapLocation.getCityCode();//城市编码
    amapLocation.getAdCode();//地区编码
    amapLocation.getAoiName();//获取当前定位点的AOI信息
    amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
    amapLocation.getFloor();//获取当前室内定位的楼层
    amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
    //获取定位时间
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date(amapLocation.getTime());
    df.format(date);
     *
     */
    private fun settingMapLocation(mapLocation: AMapLocation) {

        val aOptions = AMapOptions()

        val myLocationStyle = MyLocationStyle()
        myLocationStyle.showMyLocation(true)
        binding.mapView.map.setMyLocationStyle(myLocationStyle)

        val mLatLng = LatLng(mapLocation.latitude, mapLocation.longitude)
        val mCameraPosition = CameraPosition(mLatLng, 18F, 0F, 0F)
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(mCameraPosition)
        val marker = MarkerOptions().position(mLatLng).title("这是一个测试点位")
        binding.mapView.map.moveCamera(mCameraUpdate)
        binding.mapView.map.addMarker(marker)
    }

    private fun mapPathPlanning() {
        val routeSearch = RouteSearch(this)
        routeSearch.setRouteSearchListener(this)
        val latLngStart = LatLonPoint(31.295844, 121.438815)
        val latLngEnd = LatLonPoint(31.278277, 121.403471)
        val fromAndTo = RouteSearch.FromAndTo(latLngStart, latLngEnd)
        val query = RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, null)
        routeSearch.calculateDriveRouteAsyn(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient.onDestroy()
        mapView?.onDestroy()
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {

    }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {

    }

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {

    }

    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

    }

    private fun mLog(msg:String){
        Log.e("MapActivity",msg)
    }
}