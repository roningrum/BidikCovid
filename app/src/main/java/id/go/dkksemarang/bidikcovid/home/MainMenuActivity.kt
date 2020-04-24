package id.go.dkksemarang.bidikcovid.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.location.LocationViewModel
import id.go.dkksemarang.bidikcovid.ui.MainActivity
import id.go.dkksemarang.bidikcovid.util.GpsUtil

class MainMenuActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel

    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        GpsUtil(this)
            .turnGPSOn(object : GpsUtil.OnGpsListener {
                override fun gpsStatus(isGPSEnable: Boolean) {
                    this@MainMenuActivity.isGPSEnabled = isGPSEnable
                }

            })
        startLocationUpdate()

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
            Log.d("Location Check", "Lng : ${it.longitude}, Lat : ${it.latitude}")
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.search_menu ->{
                val intent= Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

}
