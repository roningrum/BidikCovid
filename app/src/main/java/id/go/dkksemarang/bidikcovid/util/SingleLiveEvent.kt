package id.go.dkksemarang.bidikcovid.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent <T> : MutableLiveData<T>(){
    private val mPending = AtomicBoolean(false)

    fun observer(owner: LifecycleOwner, observer: Observer<T>){
        if(hasActiveObservers()){
            Log.w(TAG, "Multiple observer registered but only one will be notified")
        }
        super.observe(owner, Observer<T>{
            if(mPending.compareAndSet(true, false)){
                observer.onChanged(it)
            }
        })
    }
    @MainThread
    override fun setValue(@Nullable value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call(){
        value = null
    }

    companion object{
        private val TAG = "SingleLiveEvent"
    }
}