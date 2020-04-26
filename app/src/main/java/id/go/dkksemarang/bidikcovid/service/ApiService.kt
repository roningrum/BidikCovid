package id.go.dkksemarang.bidikcovid.service

import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovid
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("x-username: investigasi", "x-password: pecovid")
    @GET("login")
    fun login(): Call<LoginResponse>

    @Headers("x-username:investigasi")
    @GET("pasien")
    fun pasienCovid(
        @Query("token") token: String,
        @Query("nama") nama: String
    ): Call<InfoCovidResponse>

    @Headers("x-username:investigasi")
    @FormUrlEncoded
    @POST("tambahLokasiPasien")
    fun tambahLokasiPasien(
        @Query("token") token: String,
        @Query("pasien_id") pasien_id: String,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double
    ): Call<InfoCovid>

}