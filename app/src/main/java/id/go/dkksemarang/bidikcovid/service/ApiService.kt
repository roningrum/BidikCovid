package id.go.dkksemarang.bidikcovid.service

import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers("x-username: investigasi", "x-password: pecovid")
    @GET("login")
    fun login(): Call<LoginResponse>
}