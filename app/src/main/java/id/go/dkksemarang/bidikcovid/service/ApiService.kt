package id.go.dkksemarang.bidikcovid.service

import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.pasien.model.pasienLokasi
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("x-username: investigasi", "x-password: pecovid")
    @POST("login")
    fun login(): Call<LoginResponse>

    @POST("login")
    fun loginUser(
        @Header("x-username: investigasi") username: String,
        @Header("x-password: pecovid") password: String
    ): Call<LoginResponse>


    @Headers("x-username:investigasi")
    @GET("pasien")
    fun pasienCovid(
        @Query("token") token: String,
        @Query("nama") nama: String
    ): Call<InfoCovidResponse>

    @Headers("x-username:investigasi")
    @POST("tambahLokasiPasien")
    @FormUrlEncoded
    fun tambahLokasiPasien(
        @Query("token") token: String,
        @Field("id_pasien") id_pasien: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<pasienLokasi>

}