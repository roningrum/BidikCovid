package id.go.dkksemarang.bidikcovid.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

class LocationLiveData(context: Context) : LiveData<LocationModel>(){
    private var fusedlocationClient = LocationServices.getFusedLocationProviderClient(context)

    companion object{
        val locationRequest : LocationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?: return
            for (location in locationResult.locations){
                setLocationData(location)
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1000)
                    for (address: Address in addresses) {
                        if (address.adminArea != null) {
                            getAddress(address.adminArea)
                        }
                    }
                } catch (exception: IOException) {
                    Log.e("Error", "$exception")
                }
            }
        }
    }

    private fun getAddress(adminArea: String) {
    }

    fun setLocationData(location:Location){
        value = LocationModel(
            longitude =  location.longitude,
            latitude = location.latitude
        )
    }

    fun startLocationUpdates(){
        fusedlocationClient.requestLocationUpdates(
            locationRequest, locationCallback, null
        )
    }
    override fun onActive() {
        super.onActive()
        fusedlocationClient.lastLocation.addOnSuccessListener { location : Location?->
            location?.also {
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
        fusedlocationClient.removeLocationUpdates(locationCallback)
    }

}