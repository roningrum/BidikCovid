package id.go.dkksemarang.bidikcovid.pasien.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.service.ApiClientService
import id.go.dkksemarang.bidikcovid.util.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CovidPasienViewModel: ViewModel() {
    private var state : SingleLiveEvent<CovidPasienState> = SingleLiveEvent()
    private val covidPasienList: MutableLiveData<List<InfoCovid>> = MutableLiveData()

    fun getInfoCovidPasien(token: String, nama: String) {
        val infoCovidPasienCall: Call<InfoCovidResponse> = ApiClientService().getRetrofitPasienService().pasienCovid(token, nama)
        infoCovidPasienCall.enqueue(object: Callback<InfoCovidResponse>{
            override fun onFailure(call: Call<InfoCovidResponse>, t: Throwable) {
                Log.w("Pesan", "Gagal karena ${t.message}")
                state.value = CovidPasienState.Error(t.message)
            }

            override fun onResponse(
                call: Call<InfoCovidResponse>,
                response: Response<InfoCovidResponse>
            ) {
                if(response.isSuccessful){
                    covidPasienList.value = response.body()?.infocovid
                }
                else{
                    state.value = CovidPasienState.Error("Gagal Respon")
                }
            }
        })
    }

    fun getPasienCovid(): LiveData<List<InfoCovid>> {
        return covidPasienList
    }

    fun getUiState() = state

    sealed class CovidPasienState{
        data class Error(var message: String?) : CovidPasienState()
        data class Loading(var state: Boolean = false) : CovidPasienState()
    }
}