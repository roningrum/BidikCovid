package id.go.dkksemarang.bidikcovid.ui.home.fragmentpasien

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.service.ApiClientService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasienViewModel : ViewModel() {
    private val covidPasienList: MutableLiveData<List<InfoCovid>> = MutableLiveData()

    fun getInfoCovidPasien(username: String, token: String, context: Context, status: Int) {
        val infoCovidPasienCall: Call<InfoCovidResponse> =
            ApiClientService().getRetrofitPasienService().daftarPasien(username, token, status)
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
                } else {
                    Toast.makeText(context, " ${response.body()?.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.w("Pesan", "Gagal karena ${response.body()?.message}")

                }
            }
        })
    }


    fun getPasienCovid(): LiveData<List<InfoCovid>> {
        return covidPasienList
    }
}