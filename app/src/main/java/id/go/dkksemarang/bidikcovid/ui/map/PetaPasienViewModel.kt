package id.go.dkksemarang.bidikcovid.ui.map

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.model.InfoCovid
import id.go.dkksemarang.bidikcovid.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.network.ApiClientService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetaPasienViewModel : ViewModel() {
    private val covidPasienList: MutableLiveData<List<InfoCovid>> = MutableLiveData()

    fun getPetaPasien(
        username: String,
        token: String,
        status: Int,
        flag: String,
        context: Context
    ) {
        val infoCovidPasienCall: Call<InfoCovidResponse> =
            ApiClientService().getRetrofitPasienService().petaPasien(username, token, status, flag)
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