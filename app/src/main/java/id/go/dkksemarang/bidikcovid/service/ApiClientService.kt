package id.go.dkksemarang.bidikcovid.service

import id.go.dkksemarang.bidikcovid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClientService {
    fun getClient(): OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS
        })
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
}