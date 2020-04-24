package id.go.dkksemarang.bidikcovid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.pasien.adapter.InfoCovidFilterAdapter
import id.go.dkksemarang.bidikcovid.pasien.viewmodel.CovidPasienViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var covidPasienViewModel: CovidPasienViewModel
    var adapter:InfoCovidFilterAdapter?=null

    private var token: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        covidPasienViewModel = ViewModelProviders.of(this).get(CovidPasienViewModel::class.java)
    }

//        covidPasienViewModel.getUiState().observer(this, Observer { covidPasienState ->
//            handleLoadingState(covidPasienState)
//
//        })
//
//        swipe_refresh_screen.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_orange_dark, android.R.color.holo_orange_light)
//        swipe_refresh_screen.setOnRefreshListener {
//           showCovidPasienList()
//        }
//
//        val sessionManager = SessionManager(this)
//        token = sessionManager.fetchAuthToken()
//        covidPasienViewModel.getInfoCovidPasien(token!!)
//        initView()
//        showCovidPasienList()
//    }
//
//    private fun showCovidPasienList() {
//        covidPasienViewModel.getPasienCovid().observe(this, Observer {infoCovid ->
//            if(infoCovid!=null){
//                showPasienListResult(infoCovid)
//            }
//        })
//    }
//
//    private fun handleLoadingState(covidPasienState: CovidPasienViewModel.CovidPasienState?) {
//        when(covidPasienState){
//            is CovidPasienViewModel.CovidPasienState.Error ->{
//                isLoading(false)
//                Toast.makeText(applicationContext, covidPasienState.message, Toast.LENGTH_SHORT).show()
//            }
//            is CovidPasienViewModel.CovidPasienState.Loading ->{
//                isLoading(covidPasienState.state)
//                showCovidPasienList()
//            }
//        }
//    }
//
//    private fun isLoading(state: Boolean) {
//        if(state){
//            progressBar.visibility = View.VISIBLE
//            progressBar.interpolator
//            swipe_refresh_screen.isRefreshing = true
//        }
//        else{
//            progressBar.visibility = View.INVISIBLE
//            swipe_refresh_screen.isRefreshing = false
//        }
//
//    }
//
//    private fun initView() {
//        rv_pasienList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rv_pasienList.setHasFixedSize(true)
//    }
//
//    private fun showPasienListResult(infoCovid: List<InfoCovid>) {
//        adapter = InfoCovidFilterAdapter(infoCovid as ArrayList<InfoCovid>)
//        rv_pasienList.adapter = adapter
//        adapter?.notifyDataSetChanged()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.search_menu, menu)
//        val searchItem = menu?.findItem(R.id.app_bar_search)
//        val searchView = searchItem?.actionView as SearchView
//        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter?.filter!!.filter(newText)
//                return false
//            }
//        })
//        return super.onCreateOptionsMenu(menu)
//    }


}
