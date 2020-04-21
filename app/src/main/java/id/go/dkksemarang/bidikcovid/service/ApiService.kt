package id.go.dkksemarang.bidikcovid.service

import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("x-username: investigasi", "x-password: pecovid")
    @GET("login")
    fun login(): Call<LoginResponse>

    @Headers("x-username:investigasi")
    @GET("pasien")
    fun pasienCovid(@Query("token") token: String): Call<InfoCovidResponse>
}