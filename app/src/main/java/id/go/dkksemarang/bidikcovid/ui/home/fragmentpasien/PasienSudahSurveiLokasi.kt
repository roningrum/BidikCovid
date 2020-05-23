package id.go.dkksemarang.bidikcovid.ui.home.fragmentpasien

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.model.InfoCovid
import id.go.dkksemarang.bidikcovid.ui.edit.EditPasienActivity
import id.go.dkksemarang.bidikcovid.util.SessionManager
import id.go.dkksemarang.bidikcovid.viewmodel.InfoCovidViewModel
import kotlinx.android.synthetic.main.content_layout_pasien_sudah_survei.*
import kotlinx.android.synthetic.main.fragment_pasien_sudah_survei_lokasi.*

/**
 * A simple [Fragment] subclass.
 */
class PasienSudahSurveiLokasi : Fragment() {
    private lateinit var viewModel: InfoCovidViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pasien_sudah_survei_lokasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(InfoCovidViewModel::class.java)
        val sessionManager = SessionManager(view.context)
        val token = sessionManager.fetchAuthToken()
        val username = sessionManager.fetchAuthUsername()

        observerViewModel()
        initRecyclerView()

        swipe_sudah_survei.setColorSchemeResources(
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_red_light
        )
        swipe_sudah_survei.setOnRefreshListener {
            Handler().postDelayed(Runnable {
                swipe_sudah_survei.isRefreshing = false
                observerViewModel()
            }, 5000)
        }

        val list_status = resources.getStringArray(R.array.status)
        val array_adapter =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_item, list_status)
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_status_sudah_survei.adapter = array_adapter
        spinner_status_sudah_survei.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (spinner_status_sudah_survei.selectedItem) {
                        "Semua Pasien" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "")
                        }
                        "ODP" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "1")
                        }
                        "PDP Tunggu Hasil" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "2")
                        }
                        "PDP Lab Negatif" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "4")
                        }
                        "Covid" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "5")
                        }
                        "Covid Meninggal Positif" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "6")
                        }
                        "PDP Meninggal Negatif" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "7")
                        }
                        "Corona Sembuh" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "8")
                        }
                        "ODP Negatif" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "11")
                        }
                        "PDP Meninggal Tunggu Hasil" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "71")
                        }
                        "PDP Meninggal Tidak Tunggu Hasil" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "72")
                        }
                        "Orang Tanpa Gejala" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "99")
                        }
                        "Orang Berisiko" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "100")
                        }
                        "PDP Pulang APS" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "101")
                        }
                        "OTG Negatif" -> {
                            viewModel.getPasienInfoCovidFilter(username, token, 1, "991")
                        }
                    }
                }

            }
    }

    private fun initRecyclerView() {
        rv_pasien_sudah_lokasi.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_pasien_sudah_lokasi.setHasFixedSize(true)
    }

    private fun observerViewModel() {
        viewModel.loadError.observe(viewLifecycleOwner, Observer { isError ->
            isError.let {
                if (it) {
                    Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading.let {
                pb_loading.visibility = if (it) View.VISIBLE else View.INVISIBLE
                swipe_sudah_survei.isRefreshing = it
            }

        })
        viewModel.loadZero.observe(viewLifecycleOwner, Observer { isNull ->
            isNull.let {
                if (it) {
                    pb_loading.visibility = View.INVISIBLE
                    swipe_sudah_survei.isRefreshing = false
                    tv_jumlah_data_pasien_sudah.text = "Jumlah : 0 orang"
                    hideData()
                    Toast.makeText(context, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                } else {
                    showData()
                    Toast.makeText(context, "Data Ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.pasienInfoCovid.observe(viewLifecycleOwner, Observer { infoCovid ->
            if (infoCovid.size != null) {
                tv_jumlah_data_pasien_sudah.text = "Jumlah : ${infoCovid.size} orang"
            }
            val adapter = PasienLokasiAdapter(infoCovid, this.context)
            rv_pasien_sudah_lokasi.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter.setOnItemClickCallback(object : OnItemClickCallback {
                override fun onItemClicked(infoCovid: InfoCovid) {
                    val intent = Intent(activity, EditPasienActivity::class.java)
                    intent.putExtra(EditPasienActivity.NAMA, infoCovid.nama)
                    intent.putExtra(EditPasienActivity.STATUS, infoCovid.status)
                    intent.putExtra(EditPasienActivity.ALAMAT, infoCovid.alamat)
                    intent.putExtra(EditPasienActivity.ID_PASIEN, infoCovid.pasien_id)
                    intent.putExtra(EditPasienActivity.UMUR, infoCovid.umur)
                    intent.putExtra(EditPasienActivity.JK, infoCovid.jk)
                    intent.putExtra(EditPasienActivity.LATITUDE, infoCovid.lat)
                    intent.putExtra(EditPasienActivity.LONGITUDE, infoCovid.lng)
                    startActivity(intent)
                }

            })

        })
    }

    fun hideData() {
        rv_pasien_sudah_lokasi.visibility = View.GONE
        layout_empty_data_sudah.visibility = View.VISIBLE
    }

    fun showData() {
        rv_pasien_sudah_lokasi.visibility = View.VISIBLE
        layout_empty_data_sudah.visibility = View.GONE
    }

}
