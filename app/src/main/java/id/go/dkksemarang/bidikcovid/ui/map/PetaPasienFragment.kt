package id.go.dkksemarang.bidikcovid.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.ui.home.fragmentpasien.PasienViewModel
import id.go.dkksemarang.bidikcovid.util.SessionManager

class PetaPasienFragment : Fragment(), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap
    private lateinit var pasienViewModel: PasienViewModel
    private lateinit var clusterManager: ClusterManager<InfoCovid>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_peta_pasien, container, false)
        val mapFragment: SupportMapFragment = childFragmentManager
            .findFragmentById(R.id.pasien_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pasienViewModel = ViewModelProvider(this).get(PasienViewModel::class.java)
        val sessionManager = SessionManager(view.context)
        val username = sessionManager.fetchAuthUsername()
        val token = sessionManager.fetchAuthToken()
        pasienViewModel.getInfoCovidPasien(username, token, view.context, 1)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        val semarang = LatLng(-6.992523, 110.4065905)
        pasienViewModel.getPasienCovid().observe(viewLifecycleOwner, Observer { infoCovid ->
            if (infoCovid != null) {
                showPasienMap(gMap, infoCovid)

            }
        })
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(semarang, 8f))

    }

    private fun showPasienMap(googleMap: GoogleMap, infoCovid: List<InfoCovid>) {
        clusterManager = ClusterManager(context, gMap)
        for (i in infoCovid.indices) {
            val lat = infoCovid[i].lat
            val lng = infoCovid[i].lng
            val nama = infoCovid[i].nama
            val koordinatPasien = LatLng(lat!!, lng!!)
            googleMap.addMarker(MarkerOptions().position(koordinatPasien).title(nama))
            googleMap.setOnCameraIdleListener(clusterManager)
            clusterManager.addItem(infoCovid[i])
        }
        clusterManager.cluster()

    }
}
