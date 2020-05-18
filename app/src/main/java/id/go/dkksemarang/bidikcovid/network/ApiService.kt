package id.go.dkksemarang.bidikcovid.network

import id.go.dkksemarang.bidikcovid.model.InfoCovidResponse
import id.go.dkksemarang.bidikcovid.model.LoginResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //Api Service

    //http://119.2.50.170:9095/infocovid/index.php/services/login
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

    //Buat Dapat Data Survei apa Belum
    @GET("pasien")
    fun infoPasienCovid(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Query("status") status: Int
    ): Single<InfoCovidResponse>

    //Dapat Data berdasarkan Flag
    @GET("pasien")
    fun petaPasienCovid(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Query("status") status: Int,
        @Query("flag") flag: String
    ): Single<InfoCovidResponse>

    @GET("pasien")
    fun daftarPasien(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Query("status") status: Int
    ): Call<InfoCovidResponse>

    @GET("pasien")
    fun petaPasien(
        @Header("x-username") username: String,
        @Query("token") token: String,
        @Query("status") status: Int,
        @Query("flag") flag: String
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