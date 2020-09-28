package id.go.dkksemarang.bidikcovid.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.model.LoginResponse
import id.go.dkksemarang.bidikcovid.network.ServiceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class LoginViewModel : ViewModel() {
    val loginResponses: MutableLiveData<LoginResponse> = MutableLiveData()
    private val disposable = CompositeDisposable()

    //sate
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

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
                        loading.value = true
                        if (login.status) {
                            loginResponses.value = login
                            loading.value = false
                            loadError.value = false
                            Toast.makeText(
                                context,
                                login.message,
                                Toast.LENGTH_SHORT
                            ).show()
//
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