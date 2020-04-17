package id.go.dkksemarang.bidikcovid.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.location.LocationViewModel
import id.go.dkksemarang.bidikcovid.location.util.GpsUtil
import kotlinx.android.synthetic.main.activity_tambah_pasien.*

class TambahPasienActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationViewModel: LocationViewModel
    private var isGPSEnabled = false
    private lateinit var gMap: GoogleMap

    var mapViewBundle : Bundle ?=null
    var latUser: Double = 0.0
    var lngUser: Double = 0.0

    companion object{
        const val MAP_VIEW_BUNDLE = "mapView"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pasien)

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        GpsUtil(this).turnGPSOn(object : GpsUtil.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                this@TambahPasienActivity.isGPSEnabled = isGPSEnable
            }
        })

        if(savedInstanceState!=null){
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE)
        }
        mapViewUser.onCreate(mapViewBundle)
        mapViewUser.getMapAsync(this)
        button_location.setOnClickListener {
            startLocationUpdate()
        }
    }

    override fun onStart() {
        super.onStart()
        invokeLocation()
        mapViewUser.onStart()
    }

    private fun invokeLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                !isGPSEnabled -> Toast.makeText(this, "Please Enable your GPS", Toast.LENGTH_SHORT).show()
                isPermissionGranted() -> startLocationUpdate()
                shouldShowRequestPermissionRationale() -> Toast.makeText(this, "App needs Permission", Toast.LENGTH_SHORT).show()
                else -> ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    100
                )
            }
        }
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this,
            Observer {
                latUser = it.latitude
                lngUser = it.longitude
                val lat: String = it.latitude.toString()
                val lng: String = it.longitude.toString()
                edt_location.setText("$lat, $lng")
                setUserMap(gMap, latUser, lngUser)

//            Toast.makeText(
//                this,
//                "Location Now: ${it.latitude}, ${it.longitude}",
//                Toast.LENGTH_SHORT
//            ).show()
            Log.d("Location Check", "Lng : ${it.longitude}, Lat : ${it.latitude}")
        })
    }

    private fun setUserMap(gMap: GoogleMap, latUser: Double, lngUser: Double) {
        val location = LatLng(latUser, lngUser)
        val markerOptions = MarkerOptions()
        markerOptions.position(location)

        gMap.addMarker(markerOptions)
        gMap.uiSettings.isMapToolbarEnabled = false
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15f))
    }

    private fun isPermissionGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                invokeLocation()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE)
        if(mapViewBundle == null){
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE, mapViewBundle)
            mapViewUser.onSaveInstanceState(mapViewBundle)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        setUserMap(gMap, latUser, lngUser)

    }

    override fun onResume() {
        super.onResume()
        mapViewUser.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapViewUser.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapViewUser.onPause()
    }


}
