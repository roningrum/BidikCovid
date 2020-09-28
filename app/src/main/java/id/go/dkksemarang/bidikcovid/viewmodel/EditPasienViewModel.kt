package id.go.dkksemarang.bidikcovid.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.model.PasienLokasi
import id.go.dkksemarang.bidikcovid.network.ApiClientService
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasienViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    private val covidPasienLokasi: MutableLiveData<PasienLokasi> = MutableLiveData()
    var context: Context? = null

    fun setPasienCovid(): LiveData<PasienLokasi> {
        return covidPasienLokasi
    }

    fun updateLokasiPasien(
        username: String,
        token: String,
        pasien_id: String,
        lat: Double,
        lng: Double,
        context: Context
    ) {
        this.context = context
        val updateLokasiPasien: Call<InfoCovidResponse> =
            ApiClientService().getRetrofitTambahService()
                .tambahLokasiPasien(username, token, pasien_id, lat, lng)
        updateLokasiPasien.enqueue(object : Callback<InfoCovidResponse> {
            override fun onFailure(call: Call<InfoCovidResponse>, t: Throwable) {
                Log.w("Pesan", "Gagal karena ${t.message}")
            }

            override fun onResponse(
                call: Call<InfoCovidResponse>,
                response: Response<InfoCovidResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Response tidak berhasil", Toast.LENGTH_LONG).show()
                }

            }

        })
    }

    fun updateLokasiBaruPasien(
        username: String,
        token: String,
        pasien_id: String,
        lat: Double,
        lng: Double
    ) {

    }
}