package id.go.dkksemarang.bidikcovid.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.pasien.adapter.InfoCovidAdapter
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.viewmodel.CovidPasienViewModel
import id.go.dkksemarang.bidikcovid.ui.SearchPasienActivity.Companion.SEARCH_QUERY
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.fragment_search_pasien.*
import kotlinx.android.synthetic.main.layout_not_found.*

/**
 * A simple [Fragment] subclass.
 */
class SearchPasienFragment : Fragment() {
    private lateinit var covidPasienViewModel: CovidPasienViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_pasien, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        covidPasienViewModel = ViewModelProviders.of(this).get(CovidPasienViewModel::class.java)
        covidPasienViewModel.getPasienCovid().observe(viewLifecycleOwner, Observer { infoCovid ->
            if (infoCovid != null) {
                showLoading(false)
                showCovidPasienListResult(infoCovid)
            }
            else{
                showLoading(false)
                hideData()
            }
        })

        rv_pasien_covid_List.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_pasien_covid_List.setHasFixedSize(true)

        val querySearchResult = arguments?.getString(SEARCH_QUERY)
        val sessionManager = SessionManager(view.context)
        val token = sessionManager.fetchAuthToken()
        Log.d("Token", "$token Query $querySearchResult")
        if (querySearchResult != null) {
            covidPasienViewModel.getInfoCovidPasien(token, querySearchResult)
            if(querySearchResult != querySearchResult){
                hideData()
            }
        }

    }

    private fun hideData(){
        layout_notfound.visibility = View.VISIBLE
        rv_pasien_covid_List.visibility = View.GONE
        tv_tambah_data.setOnClickListener {
            val intent = Intent(activity, TambahPasienActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun showCovidPasienListResult(infoCovid: List<InfoCovid>) {
        val adapter = InfoCovidAdapter(infoCovid, this.context)
        rv_pasien_covid_List.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun showLoading(state: Boolean) {
        if (state) {
            pb_loading.visibility = View.VISIBLE
        } else {
            pb_loading.visibility = View.GONE
        }
    }

}
