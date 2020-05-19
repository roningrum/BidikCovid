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

class PetaCovidViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    val petapasienCovid = MutableLiveData<List<InfoCovid>>()

    //state
    val loadingMarker = MutableLiveData<Boolean>()
    val handler = Handler(Looper.myLooper()!!)

    fun getSeluruhPasienPetaCovid(username: String, token: String, status: Int) {
        loadingMarker.value = true
        disposable.add(
            ServiceFactory.getApiService().infoPasienCovid(username, token, status)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<InfoCovidResponse>() {
                    override fun onSuccess(infoCovidResponse: InfoCovidResponse) {
                        handler.postDelayed({
                            if (infoCovidResponse.status) {
                                petapasienCovid.value = infoCovidResponse.infocovid
                                loadingMarker.value = false
                            } else {
                                loadingMarker.value = true
                            }
                        }, 100)
                    }

                    override fun onError(e: Throwable) {
                        loadingMarker.value = true
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