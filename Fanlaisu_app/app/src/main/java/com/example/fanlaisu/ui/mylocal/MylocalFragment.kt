package com.example.fanlaisu.ui.mylocal
import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fanlaisu.Define
import com.example.fanlaisu.MyDatabaseHelper
import kotlinx.android.synthetic.main.fragment_local.*//
import com.example.fanlaisu.R
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer

import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.example.fanlaisu.databinding.FragmentLocalBinding
import com.example.fanlaisu.dingdan
//import com.example.fanlaisu.ui.notifications.NotificationsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.example.fanlaisu.*
import org.jetbrains.annotations.Nullable

@SuppressLint("Range")
class MylocalFragment:Fragment() {

    private lateinit var mylocalViewModel: MylocalViewModel
    private var _binding: FragmentLocalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    override fun onCreateView(@NonNull inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle? ):View? {
//        var view:View = inflater.inflate(R.layout.fragment_local, container, false)
//        Log.e("=======onCreateView","onCreateView")
//        return view
//    }
////
//    override fun onViewCreated(view:View, savedInstanceState:Bundle?) {
//        super.onViewCreated(view, savedInstanceState);
//        Log.e("=========onViewCreated","onViewCreated");
//        mapView = view.findViewById(R.id.bmapView)
//    }
////
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mylocalViewModel =
            ViewModelProvider(this).get(MylocalViewModel::class.java)

        _binding = FragmentLocalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var view:View = inflater.inflate(R.layout.fragment_local, container, false)   //??????
//
        val textView: TextView = binding.textLocal
        mylocalViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
//
        return root
    }







    private var duifang="123"


//
    var mapView: MapView? = null
    protected lateinit var mLocationClient: LocationClient
    protected lateinit var locationMode: MyLocationConfiguration.LocationMode   //????????????????????????
//    protected lateinit var mlistener : MyLocationListener
    var positionText: TextView? = null
    protected lateinit var baiduMap: BaiduMap
    var isFirstLocate = true    //????????????????????????
//





    override fun onStart() {
        super.onStart()


        Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000001")
        val account_name = Define.account_name
        val dbHelper = MyDatabaseHelper(requireContext(),"User.db", 1)
        val db = dbHelper.readableDatabase
        Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000002")
        val cursor = db.query("dingdan",null,null,null,null,null,null)
        var count=0
        if(cursor.moveToFirst()){
            do {
                Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000003")
                val name1=cursor.getString(cursor.getColumnIndex("name1"))
                val name2=cursor.getString(cursor.getColumnIndex("name2"))
                val start1=cursor.getString(cursor.getColumnIndex("start1"))
                val where1=cursor.getString(cursor.getColumnIndex("where1"))
                val status = cursor.getString(cursor.getColumnIndex("status"))
                Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000005")
                if(status=="0"){
                    if (account_name==name1){
                        count=1
                        duifang=name2
                    }
                    else if (account_name==name2){
                        count=2
                        duifang=name1
                    }
                    Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000006")
                    if(count!=0){
                        Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000007")
                        var text:String="??????????????????\n"
                        if (count==1){
                            text = text+"\t????????????"+name2
                            text = text +"\n    ?????????????????????"
                        }
                        else{
                            text = text+"\n\t????????????"+name1
                            text = text + "\n\t??????????????????"
                        }
                        text = text+"\n\t?????????"+start1
                        text = text+"\n\t?????????"+where1
                        Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000008")
                        Log.d("okkkkkkkkkkkkkkkkkkk",text)
//                    val textView: TextView = requireView().findViewById()

                        dingdaning.text=text

                        Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000008")
                    }
                    Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000009")
                    break
                }
            }while (cursor.moveToNext())
        }
        cursor.close()
        chakan.setOnClickListener{
            val intent=Intent(requireActivity(),dingdan::class.java)
            startActivity(intent)
        }

        jieshudingdang.setOnClickListener{
            val cursor = db.query("dingdan",null,null,null,null,null,null)
            var count=0
            if(cursor.moveToFirst()){
                do {
                    Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000003")
                    val name1=cursor.getString(cursor.getColumnIndex("name1"))
                    val name2=cursor.getString(cursor.getColumnIndex("name2"))
                    val start1=cursor.getString(cursor.getColumnIndex("start1"))
                    val where1=cursor.getString(cursor.getColumnIndex("where1"))
                    val status = cursor.getString(cursor.getColumnIndex("status"))
                    Log.d("okkkkkkkkkkkkkkkkkkk","0000000000000000000000005")
                    if(status=="0"){
                        if (account_name==name1&&duifang==name2){
                            count=1

                            AlertDialog.Builder(requireContext()).apply {
                                setTitle("??????????????????")
                                setMessage("??????????????????????????????")
                                setCancelable(true)
                                setPositiveButton("ok") { dialog, which ->
                                    db.execSQL("update dingdan set status=? where name1= ?", arrayOf("1",account_name))
                                }
                                setNegativeButton("no"){dialog,which->}
                                show()
                            }
                            break
                        }
                        else if (account_name==name2){
                            count=2
//                            db.execSQL("update dingdan set status=? where name2= ?", arrayOf("1",account_name))
                            AlertDialog.Builder(requireContext()).apply {
                                setTitle("?????????"+name1+"????????????")
//                                setMessage("??????" + name2 + "????????????????????????")
                                setCancelable(false)
                                setPositiveButton("ok") { dialog, which ->
//                                Log.d("okkkkkkkkkkkkkkkkkkk","00000010")
//                                val intent = Intent(parent, MainActivity::class.java)
//                                intent.putExtra("help_name",name2)
//
//                                intent.putExtra("id","2")
//                                startActivity(intent)
                                }
                                show()
                            }
                        }
//
                    }
                }while (cursor.moveToNext())
            }
            cursor.close()


//            onResume()
        }


//        onstart???

//        mLocationClient = LocationClient(requireContext().applicationContext)
//        mLocationClient?.registerLocationListener(MyLocationListener()) //???????????????
//        SDKInitializer.initialize(requireContext().applicationContext)
//        SDKInitializer.setCoordType(CoordType.BD09LL);
//        setContentView(R.layout.fragment_local)//



//        mapView = bmapView
//        baiduMap = bmapView.getMap()    //??????BaiduMap??????
//        baiduMap.isMyLocationEnabled=true //??????????????????????????????
//        var permissionList = ArrayList<String>()
//        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//
//        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_PHONE_STATE)
//        }
//
//        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
//
//        if(!permissionList.isEmpty()) {
//            var permissions = permissionList.toArray(arrayOfNulls<String>(permissionList.size))
//            ActivityCompat.requestPermissions(requireActivity(),permissions,1)
//        }else {
//            requestLocation()
//        }





    }


//
//    fun initLocation() {
//        locationMode = MyLocationConfiguration.LocationMode.NORMAL
//        var option = LocationClientOption()
//        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy //???????????????
//        option.openGps = true       //??????gps
//        option.setCoorType("gcj02")    //??????????????????
//        option.setIsNeedAddress(true)   //??????????????????????????????
//        var span = 1000  //??????????????????????????????ms???<1s????????????????????????
//        option.setScanSpan(span)
//        mLocationClient?.locOption = option
//    }
//    fun navigateTo(location: BDLocation) {
//        if(isFirstLocate) {
//            Toast.makeText(requireContext(),"nav to "+location.addrStr, Toast.LENGTH_SHORT).show()
//            var ll = LatLng(location.latitude,location.longitude)   //???????????????
//            var update = MapStatusUpdateFactory.newLatLng(ll)   //??????????????????MapStatusUpdate??????
//            baiduMap?.animateMapStatus(update)  //????????????
//            update = MapStatusUpdateFactory.zoomTo(16f) //??????????????????
//            baiduMap?.animateMapStatus(update)  //??????
//            isFirstLocate = false   //??????????????????
//        }
//        var locationBuilder = MyLocationData.Builder()
//        locationBuilder.latitude(location.latitude)
//        locationBuilder.longitude(location.longitude)
//        var locationData: MyLocationData = locationBuilder.build()
//        baiduMap?.setMyLocationData(locationData)
//    }
//    fun requestLocation() {
//        initLocation()
//        mLocationClient?.start()
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when(requestCode) {
//            1 -> {
//                if(grantResults.isNotEmpty()) {
//                    for(result in grantResults) {
//                        Log.d("result:","aaaaaaaaaaaaaaaaaaaaaaaaa$result")
//                        if(result != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(requireContext(),"?????????????????????????????????????????????",Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    requestLocation()
//                }else {
//                    Toast.makeText(requireContext(),"??????????????????",Toast.LENGTH_SHORT).show()
//                }
//            }
//            else -> {
//            }
//        }
//    }
//    override fun onDestroy(){
//        super.onDestroy()
//        mLocationClient!!.stop()
//        bmapView.onDestroy()
//        baiduMap.isMyLocationEnabled=false  //????????????
//    }
//    override fun onResume() {
//        super.onResume()
//        bmapView.onResume()
//    }
//    override fun onPause() {
//        super.onPause()
//        bmapView?.onPause()
//    }
//
//
//
//
//    inner class MyLocationListener: BDLocationListener {
//        override fun onReceiveLocation(location: BDLocation?) {
//
////            runOnUiThread(object: Runnable {
////                override fun run() {
////                    var currentPosition = StringBuilder()
////                    currentPosition.append("??????: ").append(location?.latitude).append("\n")
////                    currentPosition.append("??????: ").append(location?.longitude).append("\n")
////                    currentPosition.append("????????????: ")
////                    currentPosition.append("??????: ").append(location?.country).append("\n")
////                    currentPosition.append("???: ").append(location?.province).append("\n")
////                    currentPosition.append("???: ").append(location?.city).append("\n")
////                    currentPosition.append("???: ").append(location?.district).append("\n")
////                    currentPosition.append("??????: ").append(location?.street).append("\n")
////                    if(location?.locType == BDLocation.TypeGpsLocation) {
////                        currentPosition.append("GPS")
////                    }else if(location?.locType == BDLocation.TypeNetWorkLocation) {
////                        currentPosition.append("??????")
////                    }
////                    positionText?.setText(currentPosition)
////                }
////            })
//
//            if(location != null){
//                if(location?.locType == BDLocation.TypeGpsLocation || location?.locType == BDLocation.TypeNetWorkLocation) {
//                    navigateTo(location!!)
//                }
//            }
//
//        }
//    }



}


