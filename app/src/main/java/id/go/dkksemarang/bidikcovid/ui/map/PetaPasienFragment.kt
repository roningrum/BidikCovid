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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.model.InfoCovid
import id.go.dkksemarang.bidikcovid.util.ClusterRenderer
import id.go.dkksemarang.bidikcovid.util.SessionManager
import id.go.dkksemarang.bidikcovid.viewmodel.PetaCovidViewModel

class PetaPasienFragment : Fragment(), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap
    private lateinit var petaCovidViewModel: PetaCovidViewModel
    private lateinit var clusterManager: ClusterManager<InfoCovid>
    private lateinit var marker: Marker


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
        petaCovidViewModel = ViewModelProvider(this).get(PetaCovidViewModel::class.java)
        val sessionManager = SessionManager(this.requireContext())
        val username = sessionManager.fetchAuthUsername()
        val token = sessionManager.fetchAuthToken()
        petaCovidViewModel.getSeluruhPasienPetaCovid(username, token, 1)


    }


    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        clusterManager = ClusterManager<InfoCovid>(context, gMap)
        val semarang = LatLng(-6.992523, 110.4065905)
        observeViewModel(gMap)
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(semarang, 12.0f))
        gMap.setOnCameraIdleListener(clusterManager)
        gMap.setOnMarkerClickListener(clusterManager)

        clusterManager.renderer = ClusterRenderer(context, gMap, clusterManager)


    }

    private fun observeViewModel(gMap: GoogleMap) {
        petaCovidViewModel.loadingMarker.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading.let {
                gMap.clear()
            }
        })
        petaCovidViewModel.petapasienCovid.observe(viewLifecycleOwner, Observer { petaCovid ->
            petaCovid as ArrayList
            for (i in petaCovid.indices) {
                val lat = petaCovid[i].lat
                val lng = petaCovid[i].lng
                val nama = petaCovid[i].nama
                val koordinatPasien = LatLng(lat!!, lng!!)
                marker = gMap.addMarker(
                    MarkerOptions()
                        .title(nama)
                        .position(koordinatPasien)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
                clusterManager.addItem(petaCovid[i])
            }
            clusterManager.cluster()
        })
    }
}
