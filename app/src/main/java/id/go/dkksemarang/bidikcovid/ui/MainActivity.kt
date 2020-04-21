package id.go.dkksemarang.bidikcovid.ui

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.location.LocationViewModel
import id.go.dkksemarang.bidikcovid.pasien.adapter.InfoCovidFilterAdapter
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.viewmodel.CovidPasienViewModel
import id.go.dkksemarang.bidikcovid.util.GpsUtil
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var covidPasienViewModel: CovidPasienViewModel
    internal var adapter: InfoCovidFilterAdapter?=null

    private var isGPSEnabled = false
    private var token: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        covidPasienViewModel = ViewModelProviders.of(this).get(CovidPasienViewModel::class.java)

        covidPasienViewModel.getPasienCovid().observe(this, Observer {infoCovid ->
            if(infoCovid!=null){
                showPasienListResult(infoCovid)
            }

        })
        GpsUtil(this)
            .turnGPSOn(object : GpsUtil.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                this@MainActivity.isGPSEnabled = isGPSEnable
            }

        })
        startLocationUpdate()

        val sessionManager = SessionManager(this)
        token = sessionManager.fetchAuthToken()
        rv_pasienList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_pasienList.setHasFixedSize(true)
        covidPasienViewModel.getInfoCovidPasien(token!!)
    }

    private fun showPasienListResult(infoCovid: List<InfoCovid>) {
        adapter = InfoCovidFilterAdapter(infoCovid as ArrayList<InfoCovid>)
        rv_pasienList.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        invokeLocation()
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

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
//            Toast.makeText(
//                this,
//                "Location Now: ${it.latitude}, ${it.longitude}",
//                Toast.LENGTH_SHORT
//            ).show()
            Log.d("Location Check", "Lng : ${it.longitude}, Lat : ${it.longitude}")
        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                invokeLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.app_bar_search).actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.onActionViewExpanded()
        searchView.isFocusableInTouchMode = true
        searchView.isIconified = false
        searchView.queryHint = "Cari nama pasien"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter!!.filter(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}
