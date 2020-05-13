package id.go.dkksemarang.bidikcovid.ui.home.fragmentpasien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.fragment_pasien_belum_survei_lokasi.*

/**
 * A simple [Fragment] subclass.
 */
class PasienBelumSurveiLokasi : Fragment() {
    private lateinit var pasienViewModel: PasienViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pasien_belum_survei_lokasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pasienViewModel = ViewModelProvider(this).get(PasienViewModel::class.java)
        pasienViewModel.getPasienCovid().observe(viewLifecycleOwner, Observer { infoPasien ->
            if (infoPasien != null) {
                showPasienList(infoPasien)
                progressBar.visibility = View.INVISIBLE
            }
        })
        rv_pasien_null_lokasi.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_pasien_null_lokasi.setHasFixedSize(true)

        swipe.setOnRefreshListener {
            pasienViewModel.getPasienCovid().observe(viewLifecycleOwner, Observer { infoPasien ->
                if (infoPasien != null) {
                    showPasienList(infoPasien)
                }
            })
        }

        val sessionManager = SessionManager(view.context)
        val token = sessionManager.fetchAuthToken()
        val username = sessionManager.fetchAuthUsername()
        pasienViewModel.getInfoCovidPasien(username, token, view.context, 0)
    }

    private fun showPasienList(infoPasien: List<InfoCovid>) {

        val adapter = PasienLokasiAdapter(infoPasien, this.context)
        rv_pasien_null_lokasi.adapter = adapter
        adapter.notifyDataSetChanged()
    }


}
