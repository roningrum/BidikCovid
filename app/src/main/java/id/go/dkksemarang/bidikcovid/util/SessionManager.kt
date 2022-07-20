package id.go.dkksemarang.bidikcovid.util

import android.content.Context
import android.content.SharedPreferences
import id.go.dkksemarang.bidikcovid.R

class SessionManager(context: Context){
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN ="user_token"
        const val USERNAME = "username"
        const val LOKASI_USER = "lokasi_user"
    }


    fun saveAuthUsername(username: String) {
        val editor = prefs.edit()
        editor.putString(USERNAME, username)
        editor.apply()
        editor.commit()
    }

    fun fetchAuthUsername(): String {
        return prefs.getString(USERNAME, "")!!
    }

    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
        editor.commit()
    }

    fun fetchAuthToken(): String{
        return prefs.getString(USER_TOKEN, "")!!
    }
    fun logout(username: String) {
        val editor = prefs.edit()
        editor.remove(username)
        editor.clear()
        editor.apply()
    }


}