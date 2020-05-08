package id.go.dkksemarang.bidikcovid.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dkksemarang.bidikcovid.PasienCovidDetail
import id.go.dkksemarang.bidikcovid.PasienCovidDetail.Companion.ALAMAT
import id.go.dkksemarang.bidikcovid.PasienCovidDetail.Companion.ID_PASIEN
import id.go.dkksemarang.bidikcovid.PasienCovidDetail.Companion.JK
import id.go.dkksemarang.bidikcovid.PasienCovidDetail.Companion.STATUS
import id.go.dkksemarang.bidikcovid.PasienCovidDetail.Companion.UMUR
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.pasien.adapter.InfoCovidAdapter
import id.go.dkksemarang.bidikcovid.pasien.adapter.OnItemClickCallback
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.viewmodel.CovidPasienViewModel
import id.go.dkksemarang.bidikcovid.ui.SearchPasienActivity.Companion.SEARCH_QUERY
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.fragment_search_pasien.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
class SearchPasienFragment : Fragment() {
    private lateinit var covidPasienViewModel: CovidPasienViewModel
    private  var  querySearchResult: String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_pasien, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        covidPasienViewModel = ViewModelProviders.of(this).get(CovidPasienViewModel::class.java)
        covidPasienViewModel.getPasienCovid().observe(viewLifecycleOwner, Observer { infoCovid ->
            if (infoCovid != null) {
                showCovidPasienListResult(infoCovid)
            }
            else{
                hideData()
                showLoading(false)
            }
        })

        rv_pasien_covid_List.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_pasien_covid_List.setHasFixedSize(true)

        querySearchResult = arguments?.getString(SEARCH_QUERY)
        val sessionManager = SessionManager(view.context)
        val token = sessionManager.fetchAuthToken()
        val username = sessionManager.fetchAuthUsername()

        Log.d("Token", "$token Query $querySearchResult")
        if (querySearchResult != null) {
            covidPasienViewModel.getInfoCovidPasien(username, token,  querySearchResult!!)
        }

    }

    fun hideData(){
        layout_notfound.visibility = View.VISIBLE
        rv_pasien_covid_List.visibility = View.GONE

    }

    private fun showCovidPasienListResult(infoCovid: List<InfoCovid>) {
        val handler = Handler()
        handler.postDelayed({
            val adapter = InfoCovidAdapter(infoCovid, this.context)
            rv_pasien_covid_List.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter.setOnItemClickCallback(object : OnItemClickCallback{
                override fun onItemClicked(infoCovid: InfoCovid) {
                    val intent = Intent(activity, PasienCovidDetail::class.java)
                    intent.putExtra(PasienCovidDetail.NAMA, infoCovid.nama)
                    intent.putExtra(STATUS,infoCovid.status)
                    intent.putExtra(ALAMAT, infoCovid.alamat)
                    intent.putExtra(ID_PASIEN, infoCovid.pasien_id)
                    intent.putExtra(UMUR, infoCovid.umur)
                    intent.putExtra(JK, infoCovid.jk)
                    startActivity(intent)
                }
            })
        }, 500)
        showLoading(false)

    }

    fun showLoading(state: Boolean) {
        if (state) {
            pb_loading.visibility = View.VISIBLE
            pb_loading.playAnimation()
            tv_waiting_text.visibility = View.VISIBLE
        } else {
            pb_loading.pauseAnimation()
            pb_loading.visibility = View.GONE
            tv_waiting_text.visibility = View.GONE
        }
    }



}
