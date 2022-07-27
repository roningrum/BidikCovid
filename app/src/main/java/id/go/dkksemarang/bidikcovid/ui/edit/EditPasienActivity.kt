package id.go.dkksemarang.bidikcovid.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import id.go.dkksemarang.bidikcovid.EditPasienMapActivity
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.model.PasienLokasi
import id.go.dkksemarang.bidikcovid.ui.map.PetaPasienFragment
import id.go.dkksemarang.bidikcovid.ui.search.SearchPasienActivity
import id.go.dkksemarang.bidikcovid.util.SessionManager
import id.go.dkksemarang.bidikcovid.viewmodel.EditPasienViewModel
import kotlinx.android.synthetic.main.content_edit_pasien.*

class EditPasienActivity : AppCompatActivity() {
    companion object {
        const val NAMA = "Nama"
        const val STATUS = "Status"
        const val JK = "JK"
        const val ALAMAT = "Alamat"
        const val ID_PASIEN = "IDPASIEN"
        const val LATITUDE = "LAT"
        const val LONGITUDE = "LNG"
        const val UMUR = "UMUR"
        const val PLACE_PICKER_REQUEST = 1
    }

    private lateinit var editPasienViewModel: EditPasienViewModel

    var nama: String? = null
    var umur: String? = null
    var alamat: String? = null
    var jk: String? = null
    var pasien_id: String? = null
    var status: String? = null

    var lat: Double? = null
    var lng: Double? = null
    var token: String? = null
    var username: String? = null

//    var lat_update:Double?=null
//    var lng_update:Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pasien)

        editPasienViewModel = ViewModelProviders.of(this).get(EditPasienViewModel::class.java)
        initDataPasien()
        val sessionManager = SessionManager(this)
        token = sessionManager.fetchAuthToken()
        username = sessionManager.fetchAuthUsername()
//
//        lat_update = intent.getDoubleExtra("lat_pasien_update",0.0)
//        lng_update = intent?.getDoubleExtra("lng_pasien_update",0.0)
//        simpanUpdateLokasiPasien(lat_update, lng_update)

        editPasienViewModel.setPasienCovid().observe(this, Observer { pasienLokasi ->
            if (pasienLokasi.id_pasien != null) {
                updateDataPasien(pasienLokasi)
            }

        })


    }

    private fun initDataPasien() {
        nama = intent.getStringExtra(NAMA)
        status = intent.getStringExtra(STATUS)
        jk = intent.getStringExtra(JK)
        alamat = intent.getStringExtra(ALAMAT)
        umur = intent.getStringExtra(UMUR)
        lat = intent.getDoubleExtra(LATITUDE, 0.0)
        lng = intent.getDoubleExtra(LONGITUDE, 0.0)
        pasien_id = intent.getStringExtra(ID_PASIEN)

        tv_detail_nama_pasien.text = nama
        tv_detail_alamat_pasien.text = alamat
        tv_detail_id_pasien.text = pasien_id
        tv_detail_status_pasien.text = status

        if (jk == "L") {
            tv_detail_jenis_kelamin_pasien.text = "Laki-laki"
        } else {
            tv_detail_jenis_kelamin_pasien.text = "Perempuan"
        }
        if (lat == 0.0 && lng == 0.0) {
            tv_detail_lokasi_pasien.text = "null, null"
        } else {
            tv_detail_lokasi_pasien.text = "$lat, $lng"
        }

        btn_edit_lokasi_pasien.setOnClickListener {
            val intent = Intent(this, EditPasienMapActivity::class.java)
            startActivityForResult(intent, 1)
//            val builder = PlacePicker.IntentBuilder()
//            try {
//                startActivityForResult(
//                    builder.build(this@EditPasienActivity),
//                    PLACE_PICKER_REQUEST
//                )
//            } catch (e: GooglePlayServicesRepairableException) {
//                e.printStackTrace()
//            } catch (e: GooglePlayServicesNotAvailableException) {
//                e.printStackTrace()
//            }
        }
    }

    private fun updateDataPasien(pasienLokasi: PasienLokasi) {
        pasienLokasi.latitude = lat
        pasienLokasi.longitude = lng
        pasienLokasi.id_pasien = pasien_id
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            lat = data?.getDoubleExtra("lat_pasien_update",0.0)
            lng = data?.getDoubleExtra("lng_pasien_update",0.0)
            val result = "$lat,$lng"
            tv_detail_lokasi_pasien.text = result
            simpanUpdateLokasiPasien(lat, lng)

//            if (resultCode == Activity.RESULT_OK) {
//                val place = PlacePicker.getPlace(data, this)
//                lat = place.latLng.latitude
//                lng = place.latLng.longitude
//                val result = "$lat,$lng"
//                tv_detail_lokasi_pasien.text = result
//                simpanUpdateLokasiPasien(lat, lng)
//            }
        }
    }

    private fun simpanUpdateLokasiPasien(lat: Double?, lng: Double?) {
        btn_update_pasien.setOnClickListener {
            editPasienViewModel.updateLokasiPasien(
                username!!,
                token!!,
                pasien_id!!,
                lat!!,
                lng!!,
                this
            )
        }

    }
}