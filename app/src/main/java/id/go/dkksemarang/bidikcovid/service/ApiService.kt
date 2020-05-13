package id.go.dkksemarang.bidikcovid.service

import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import id.go.dkksemarang.bidikcovid.pasien.model.InfoCovidResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

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

    @GET("pasien")
    fun daftarPasien(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Query("status") status: Int
    ): Call<InfoCovidResponse>

    @POST("tambahLokasiPasien")
    @FormUrlEncoded
    fun tambahLokasiPasien(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Field("id_pasien") id_pasien: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<InfoCovidResponse>

}