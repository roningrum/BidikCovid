package id.go.dkksemarang.bidikcovid.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.model.InfoCovid
import id.go.dkksemarang.bidikcovid.ui.edit.EditPasienActivity
import id.go.dkksemarang.bidikcovid.ui.search.SearchPasienActivity.Companion.SEARCH_QUERY
import id.go.dkksemarang.bidikcovid.util.SessionManager
import id.go.dkksemarang.bidikcovid.viewmodel.SearchPasienViewModel
import kotlinx.android.synthetic.main.fragment_search_pasien.*

/**
 * A simple [Fragment] subclass.
 */
class SearchPasienFragment : Fragment() {
    private lateinit var searchCovidViewModel: SearchPasienViewModel
    private var querySearchResult: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_pasien, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchCovidViewModel = ViewModelProviders.of(this).get(SearchPasienViewModel::class.java)
        observerViewModel()

        rv_pasien_covid_List.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_pasien_covid_List.setHasFixedSize(true)

        querySearchResult = arguments?.getString(SEARCH_QUERY)
        val sessionManager = SessionManager(view.context)
        val token = sessionManager.fetchAuthToken()
        val username = sessionManager.fetchAuthUsername()

        Log.d("Token", "$token Query $querySearchResult")
        if (querySearchResult != null) {
            searchCovidViewModel.searchPasienResult(username, token, querySearchResult!!)
        }

    }

    private fun observerViewModel() {
        searchCovidViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading.let {
                if (it) {
                    pb_loading_search.visibility = View.VISIBLE
                    pb_loading_search.playAnimation()
                    tv_waiting_text.visibility = View.VISIBLE
                } else {
                    pb_loading_search.pauseAnimation()
                    pb_loading_search.visibility = View.GONE
                    tv_waiting_text.visibility = View.GONE
                }
            }
        })
        searchCovidViewModel.loadZero.observe(viewLifecycleOwner, Observer { isNull ->
            isNull.let {
                if (it) {
                    pb_loading_search.pauseAnimation()
                    pb_loading_search.visibility = View.GONE
                    tv_waiting_text.visibility = View.GONE
                    hideData()
                    Toast.makeText(context, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Data Ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        })
        searchCovidViewModel.loadError.observe(viewLifecycleOwner, Observer { isError ->
            isError.let {
                if (it) {
                    Toast.makeText(context, "Gagal Memuat Data", Toast.LENGTH_SHORT).show()
                }
            }
        })
        searchCovidViewModel.pasienInfoCovidResult.observe(
            viewLifecycleOwner,
            Observer { infoCovid ->
                val adapter =
                    InfoCovidAdapter(
                        infoCovid,
                        this.context
                    )
                rv_pasien_covid_List.adapter = adapter
                adapter.notifyDataSetChanged()
                adapter.setOnItemClickCallback(object :
                    OnItemClickCallback {
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
        layout_notfound.visibility = View.VISIBLE
        rv_pasien_covid_List.visibility = View.GONE
    }


}
