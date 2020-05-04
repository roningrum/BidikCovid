package id.go.dkksemarang.bidikcovid

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import id.go.dkksemarang.bidikcovid.location.LocationViewModel
import id.go.dkksemarang.bidikcovid.pasien.model.pasienLokasi
import id.go.dkksemarang.bidikcovid.pasien.viewmodel.CovidPasienViewModel
import id.go.dkksemarang.bidikcovid.ui.TambahPasienActivity
import id.go.dkksemarang.bidikcovid.util.GpsUtil
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.activity_pasien_covid_detail.*
import kotlinx.android.synthetic.main.activity_pasien_covid_detail.mapViewUser
import kotlinx.android.synthetic.main.activity_tambah_pasien.edt_location
import kotlinx.android.synthetic.main.content_location_layout.*

class PasienCovidDetail : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val NAMA = "Nama"
        const val STATUS = "Status"
        const val JK = "JK"
        const val ALAMAT = "Alamat"
        const val ID_PASIEN = "IDPASIEN"
        const val UMUR = "UMUR"
        const val MAP_VIEW_BUNDLE = "mapView"
    }

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var covidPasienViewModel: CovidPasienViewModel
    private var isGPSEnabled = false
    private lateinit var gMap: GoogleMap

    var mapViewBundle: Bundle? = null
    var nama: String? = null
    var umur: String? = null
    var alamat: String? = null
    var jk: String? = null
    var pasien_id: String? = null
    var status: String? = null

    var latUser: Double = 0.0
    var lngUser: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasien_covid_detail)

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        covidPasienViewModel = ViewModelProviders.of(this).get(CovidPasienViewModel::class.java)

        nama = intent.getStringExtra(NAMA)
        status = intent.getStringExtra(STATUS)
        jk = intent.getStringExtra(JK)
        alamat = intent.getStringExtra(ALAMAT)
        umur = intent.getStringExtra(UMUR)
        pasien_id = intent.getStringExtra(ID_PASIEN)

        tv_nama_pasien_detail.text = nama
        tv_alamat_pasien.text = alamat
        tv_umur_pasien.text = umur
        tv_pasien_id.text = pasien_id
        if (jk == "L") {
            tv_jenis_kelamin.text = "Laki-Laki"
        } else {
            tv_jenis_kelamin.text = "Perempuan"
        }
        tv_status_pasien.text = status

        val sessionManager = SessionManager(this)
        latUser = sessionManager.fetchLokasiLat()
        lngUser = sessionManager.fetchLokasiLng()
        val token = sessionManager.fetchAuthToken()

        covidPasienViewModel.setPasienCovid().observe(this, Observer { pasienLokasi ->
            updateDataPasien(pasienLokasi)
        })
        GpsUtil(this)
            .turnGPSOn(object : GpsUtil.OnGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    this@PasienCovidDetail.isGPSEnabled = isGPSEnable
                }
            })

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(TambahPasienActivity.MAP_VIEW_BUNDLE)
        }
        mapViewUser.onCreate(mapViewBundle)
        mapViewUser.getMapAsync(this)
        edt_location.showSoftInputOnFocus = false
        btn_update_location.setOnClickListener {
            startLocationUpdate()
        }

        btn_update_data.setOnClickListener {
            covidPasienViewModel.updateLokasiPasien(token, pasien_id!!, latUser, lngUser)
            Toast.makeText(this, "Lokasi telah terupdate", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateDataPasien(pasienLokasi: pasienLokasi) {
        when {
            pasienLokasi.id_pasien != null -> {
                Toast.makeText(this, "Data sudah ada", Toast.LENGTH_SHORT).show()
            }
            else -> {
                pasienLokasi.latitude = latUser
                pasienLokasi.longitude = lngUser
                pasienLokasi.id_pasien = pasien_id
            }
        }

    }


    override fun onStart() {
        super.onStart()
        invokeLocation()
        mapViewUser.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapViewBundle = outState.getBundle(TambahPasienActivity.MAP_VIEW_BUNDLE)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(TambahPasienActivity.MAP_VIEW_BUNDLE, mapViewBundle)
            mapViewUser.onSaveInstanceState(mapViewBundle)
        }
    }

    private fun invokeLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                !isGPSEnabled -> Toast.makeText(this, "Please Enable your GPS", Toast.LENGTH_SHORT)
                    .show()
                isPermissionGranted() -> startLocationUpdate()
                shouldShowRequestPermissionRationale() -> Toast.makeText(
                    this,
                    "App needs Permission",
                    Toast.LENGTH_SHORT
                ).show()
                else -> ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    100
                )
            }
        }
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this,
            Observer {
                if (it != null) {
                    latUser = it.latitude
                    lngUser = it.longitude
                    val lat: String = it.latitude.toString()
                    val lng: String = it.longitude.toString()
                    edt_location.setText("$lat, $lng")
                    setUserMap(gMap, latUser, lngUser)

                    Log.d("Location Check", "Lng : ${latUser}, Lat : ${lngUser}")
                }

            })
    }

    private fun setUserMap(gMap: GoogleMap, latUser: Double, lngUser: Double) {
        val location = LatLng(latUser, lngUser)
        val markerOptions = MarkerOptions()
        markerOptions.position(location)
        gMap.clear()
        gMap.addMarker(markerOptions)
        gMap.uiSettings.isMapToolbarEnabled = false
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                invokeLocation()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapViewBundle = outState.getBundle(TambahPasienActivity.MAP_VIEW_BUNDLE)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(TambahPasienActivity.MAP_VIEW_BUNDLE, mapViewBundle)
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
