package id.go.dkksemarang.bidikcovid.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.model.LoginResponse
import id.go.dkksemarang.bidikcovid.network.ApiClientService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val loginResponses: MutableLiveData<LoginResponse> = MutableLiveData()

    fun getLoginUserResponse(username: String, password: String) {
        val loginResponse: Call<LoginResponse> =
            ApiClientService().getRetrofitLoginService().loginUser(username, password)
        loginResponse.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Gagal Masuk", "Pesan ${t.message}")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    loginResponses.value = response.body()
                    Log.d("Token", "Tokenku${loginResponses.value?.token}")
                }
            }

        })
    }

    fun getLogin(): LiveData<LoginResponse> {
        return loginResponses
    }
}