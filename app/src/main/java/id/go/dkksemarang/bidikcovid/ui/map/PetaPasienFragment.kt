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

    var flag: String? = null

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
//        pasienViewModel = ViewModelProvider(this).get(PetaPasienViewModel::class.java)
        petaCovidViewModel = ViewModelProvider(this).get(PetaCovidViewModel::class.java)
        val sessionManager = SessionManager(this.requireContext())
        val username = sessionManager.fetchAuthUsername()
        val token = sessionManager.fetchAuthToken()
        petaCovidViewModel.getSeluruhPasienPetaCovid(username, token, 1)


//        val filter_pasien = resources.getStringArray(R.array.status)
//        val adapter_filter_pasien =
//            ArrayAdapter(view.context, android.R.layout.simple_spinner_item, filter_pasien)
//        adapter_filter_pasien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner_filter_data.adapter = adapter_filter_pasien

//        spinner_filter_data.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//
//                if (spinner_filter_data.selectedItem.equals("ODP")) {
//                    showMarkerBased("1")
//                } else if (spinner_filter_data.selectedItem.equals("Corona Sembuh")) {
//                    showMarkerBased("4")
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }
//
//        adapter_filter_pasien.notifyDataSetChanged()


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
