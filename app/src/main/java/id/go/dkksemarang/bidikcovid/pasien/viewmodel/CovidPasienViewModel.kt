package id.go.dkksemarang.bidikcovid.pasien.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.pasien.model.pasienLokasi
import id.go.dkksemarang.bidikcovid.service.ApiClientService
import id.go.dkksemarang.bidikcovid.ui.SearchPasienFragment
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


    fun getPasienCovid(): LiveData<List<InfoCovid>> {
        return covidPasienList
    }

    fun setPasienCovid(): LiveData<pasienLokasi> {
        return covidPasienLokasi
    }

    fun updateLokasiPasien(
        username: String,
        token: String,
        pasien_id: String,
        lat: Double,
        lng: Double
    ) {
        val updateLokasiPasien: Call<pasienLokasi> = ApiClientService().getRetrofitTambahService()
            .tambahLokasiPasien(username, token, pasien_id, lat, lng)
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