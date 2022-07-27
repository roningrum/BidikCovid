package id.go.dkksemarang.bidikcovid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import id.go.dkksemarang.bidikcovid.ui.edit.EditPasienActivity
import kotlinx.android.synthetic.main.activity_edit_pasien_map.*

class EditPasienMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener {
    private lateinit var mMap : GoogleMap
    private lateinit var startMarker: Marker
    private var lat: Double?=null
    private var long: Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pasien_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.picker_user_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        submitLocationBtn.setOnClickListener {
            val i = Intent(this, EditPasienActivity::class.java)
            i.putExtra("lat_pasien_update", lat)
            i.putExtra("lng_pasien_update", long)
            setResult(1, i)
            finish()
//            Toast.makeText(this, "Lokasi saat ini $lat, $long", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)
//        mMap.isMyLocationEnabled = true
        val semarang = LatLng(-6.9864547,110.4156647)
        startMarker = mMap.addMarker(MarkerOptions()
            .position(semarang)
            .draggable(true))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(semarang, 14.0f))

        mMap.setOnMarkerDragListener(this)

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {
        Toast.makeText(this, "Changing Location", Toast.LENGTH_SHORT).show()
    }



    override fun onMarkerDragEnd(p0: Marker?) {
        if(p0 == startMarker){
            p0.position?.let { setStartLocation(it.latitude, it.longitude) }
        }

    }

    private fun setStartLocation(latitude: Double, longitude: Double) {
        mMap.addMarker(MarkerOptions()
            .position(LatLng(latitude, longitude)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 14.0f))
        lat = latitude
        long= longitude
        Log.d("New", "Location $lat, $long")
    }

//    private fun setFinishLocation(latitude: Double, longitude: Double, s: String) {
//        finishMarker = mMap.addMarker(
//            MarkerOptions().position(LatLng(latitude, longitude))
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 14.0f))
//    }


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
//           if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//               Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
//           }
//            else{
//               Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}



