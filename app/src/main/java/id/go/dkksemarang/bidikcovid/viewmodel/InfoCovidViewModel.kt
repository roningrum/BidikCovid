package id.go.dkksemarang.bidikcovid.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.go.dkksemarang.bidikcovid.model.InfoCovid
import id.go.dkksemarang.bidikcovid.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.network.ServiceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class InfoCovidViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    val pasienInfoCovid = MutableLiveData<List<InfoCovid>>()

    //state
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val loadZero = MutableLiveData<Boolean>()

    val handler = Handler(Looper.myLooper()!!)

    fun getPasienInfoCovidFilter(username: String, token: String, status: Int, flag: String) {
        loading.value = true

        disposable.add(
            ServiceFactory.getApiService().petaPasienCovid(username, token, status, flag)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<InfoCovidResponse>() {
                    override fun onSuccess(infoCovidResponse: InfoCovidResponse) {
                        handler.postDelayed(Runnable {
                            if (infoCovidResponse.status) {
                                pasienInfoCovid.value = infoCovidResponse.infocovid
                                loading.value = false
                                loadZero.value = false
                            } else {
                                loading.value = true
                                loadZero.value = true
                            }
                        }, 5000)

                    }

                    override fun onError(e: Throwable) {
                        handler.postDelayed(Runnable {
                            loadError.value = true
                            loading.value = false
                            e.printStackTrace()
                        }, 5000)

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