package id.go.dkksemarang.bidikcovid.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.model.LoginResponse
import id.go.dkksemarang.bidikcovid.network.ApiClientService
import id.go.dkksemarang.bidikcovid.network.ServiceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    val loginResponses: MutableLiveData<LoginResponse> = MutableLiveData()
    private val disposable = CompositeDisposable()

    //sate
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    val handler = Handler(Looper.myLooper()!!)

    fun getLoginUserResponse(username: String, password: String) {
        val loginResponse: Call<LoginResponse> =
            ApiClientService().getRetrofitLoginService().loginUser(username, password)
        loginResponse.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Gagal Masuk", "Pesan ${t.message}")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    loginResponses.value = response.body()
                    Log.d("Token", "Tokenku${loginResponses.value?.token}")
                }
            }

        })
    }

    fun getLogin(): LiveData<LoginResponse> {
        return loginResponses
    }

    fun loginUser(username: String, password: String, context: Context) {
        disposable.add(
            ServiceFactory.getApiService().login(username, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {
                        loading.value = false
                    }

                    override fun onNext(login: LoginResponse) {
                        if (login.status) {
                            loginResponses.value = login
                            loading.value = false
                            loadError.value = false
                        } else {
                            Toast.makeText(
                                context,
                                "Gagal Login karena ${login.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        disposable.dispose()
    }
}