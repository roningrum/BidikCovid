package id.go.dkksemarang.bidikcovid.service

import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.pasien.model.pasienLokasi
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    @Headers("x-username: investigasi", "x-password: pecovid")
//    @POST("login")
//    fun login(): Call<LoginResponse>

    @GET("login")
    fun loginUser(
        @Header("x-username") username: String,
        @Header("x-password") password: String
    ): Call<LoginResponse>


    @GET("pasien")
    fun pasienCovid(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Query("nama") nama: String
    ): Call<InfoCovidResponse>

//    @GET("pasien")
//    fun pasienCovidS(
//        @Header("x-username") username: String,
//        @Query("token") token: String,
//        @Query("nama") nama: String
//    ): Call<String>


    @POST("tambahLokasiPasien")
    @FormUrlEncoded
    fun tambahLokasiPasien(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Field("id_pasien") id_pasien: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<pasienLokasi>

}