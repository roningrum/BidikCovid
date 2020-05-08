package id.go.dkksemarang.bidikcovid.pasien.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.pasien.model.pasienLokasi
import id.go.dkksemarang.bidikcovid.service.ApiClientService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CovidPasienViewModel : ViewModel() {
    private val covidPasienList: MutableLiveData<List<InfoCovid>> = MutableLiveData()
    private val covidPasienLokasi: MutableLiveData<pasienLokasi> = MutableLiveData()

    fun getInfoCovidPasien(username: String, token: String, nama: String) {
        val infoCovidPasienCall: Call<InfoCovidResponse> =
            ApiClientService().getRetrofitPasienService().pasienCovid(username, token, nama)
        infoCovidPasienCall.enqueue(object : Callback<InfoCovidResponse> {
            override fun onFailure(call: Call<InfoCovidResponse>, t: Throwable) {
                Log.w("Pesan", "Gagal karena ${t.message}")
            }

            override fun onResponse(
                call: Call<InfoCovidResponse>,
                response: Response<InfoCovidResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    covidPasienList.value = response.body()?.infocovid
                    Log.w("Username", "Username $username")
                } else {
                    Log.w("Pesan", "Gagal karena ${response.body()?.message}")
                }
            }
        })
    }

//    fun getInfoCovidPasienS(username: String, token: String, nama: String) {
//        val infoCovidPasienCall: Call<String> =
//            ApiClientService().getRetrofitPasienService().pasienCovidS(token, nama, username)
//        infoCovidPasienCall.enqueue(object : Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.d("Pesan", "Gagal karena ${t.message}")
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.d("Pesan", "Hasil karena ${call.isExecuted}")
//            }
//
//        })
//    }
//
//    fun getPasienCovidS(): LiveData<String> {
//        return covidPasienS
//    }

    fun getPasienCovid(): LiveData<List<InfoCovid>> {
        return covidPasienList
    }

    fun setPasienCovid(): LiveData<pasienLokasi> {
        return covidPasienLokasi
    }

    fun updateLokasiPasien(
        token: String,
        username: String,
        pasien_id: String,
        lat: Double,
        lng: Double
    ) {
        val updateLokasiPasien: Call<pasienLokasi> = ApiClientService().getRetrofitTambahService()
            .tambahLokasiPasien(token, username, pasien_id, lat, lng)
        updateLokasiPasien.enqueue(object : Callback<pasienLokasi> {
            override fun onFailure(call: Call<pasienLokasi>, t: Throwable) {
                Log.w("Pesan", "Gagal karena ${t.message}")
            }

            override fun onResponse(call: Call<pasienLokasi>, response: Response<pasienLokasi>) {
                if (response.isSuccessful) {
                    covidPasienLokasi.value = response.body()
                }

            }

        })
    }
}