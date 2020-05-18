package id.go.dkksemarang.bidikcovid.network

import com.google.gson.GsonBuilder
import id.go.dkksemarang.bidikcovid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClientService {

    private val interceptor = HttpLoggingInterceptor()

    fun getClient(): OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS))
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getRetrofitLogin(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL_COVID)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRetrofitLoginService():ApiService{
        return getRetrofitLogin().create(ApiService::class.java)
    }

    fun getPasienClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        })
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getRetrofitPasien(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL_COVID)
            .client(getPasienClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getRetrofitPasienService(): ApiService {
        return getRetrofitPasien().create(ApiService::class.java)
    }

    fun getRetrofitTambahService(): ApiService{
        return getRetrofitPasien().create(ApiService::class.java)
    }


}