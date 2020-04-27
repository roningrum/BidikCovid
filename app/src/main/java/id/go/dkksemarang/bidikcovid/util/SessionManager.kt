package id.go.dkksemarang.bidikcovid.util

import android.content.Context
import android.content.SharedPreferences
import id.go.dkksemarang.bidikcovid.R

class SessionManager(context: Context){
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN ="user_token"
        const val LOKASI_USER = "lokasi_user"
    }

    fun saveLokasiLat(lat: Double){
        val editor = prefs.edit()
        editor.putFloat(LOKASI_USER, lat.toFloat())
        editor.apply()
    }

    fun fetchLokasiLat(): Double{
        return prefs.getFloat(LOKASI_USER, 0.0F).toDouble()
    }

    fun saveLokasiLng(lng: Double){
        val editor = prefs.edit()
        editor.putFloat(LOKASI_USER, lng.toFloat())
        editor.apply()
    }

    fun fetchLokasiLng(): Double{
        return prefs.getFloat(LOKASI_USER, 0.0F).toDouble()
    }

    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String{
        return prefs.getString(USER_TOKEN, "")!!
    }


}