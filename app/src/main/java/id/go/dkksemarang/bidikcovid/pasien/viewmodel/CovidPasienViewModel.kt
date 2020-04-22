package id.go.dkksemarang.bidikcovid.pasien.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.service.ApiClientService
import id.go.dkksemarang.bidikcovid.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CovidPasienViewModel(val view: MainActivity) : ViewModel() {
    private val covidPasienList: MutableLiveData<List<InfoCovid>> = MutableLiveData()

    fun getInfoCovidPasien(token: String) {
        val infoCovidPasienCall: Call<InfoCovidResponse> = ApiClientService().getRetrofitPasienService().pasienCovid(token)
        infoCovidPasienCall.enqueue(object: Callback<InfoCovidResponse>{
            override fun onFailure(call: Call<InfoCovidResponse>, t: Throwable) {
                Log.w("Pesan", "Gagal karena ${t.message}")
            }

            override fun onResponse(
                call: Call<InfoCovidResponse>,
                response: Response<InfoCovidResponse>
            ) {
                if(response.isSuccessful){
                    covidPasienList.value = response.body()?.infocovid
                }
            }
        })
    }

    fun getPasienCovid(): LiveData<List<InfoCovid>> {
        return covidPasienList
    }
}