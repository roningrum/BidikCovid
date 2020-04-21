package id.go.dkksemarang.bidikcovid.login.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import id.go.dkksemarang.bidikcovid.service.ApiClientService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val loginResponses: MutableLiveData<LoginResponse> = MutableLiveData()

    fun getLoginResponse() {
        val loginResponse: Call<LoginResponse> = ApiClientService().getRetrofitLoginService().login()
        loginResponse.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val view: View? = null
                loginResponses.value = null
                Toast.makeText(view?.context, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loginResponses.value = response.body()
                Log.d("Token", "Tokenku${loginResponses.value?.token}")
            }
        })
    }

    fun getLogin(): LiveData<LoginResponse> {
        return loginResponses
    }
}