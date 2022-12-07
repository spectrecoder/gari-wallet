package io.gari.sample.network

import com.google.gson.Gson
import io.gari.sample.network.service.LoginApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkProvider(
    private val httpClientBuilder: HttpClientBuilder,
    private val gson: Gson
) {

    fun provideLoginService(): LoginApiService {
        return provideApiService(
            Api.Url.DUMMY_TOKEN_DOMAIN,
            LoginApiService::class.java
        )
    }

    private fun <T> provideApiService(baseUrl: String, service: Class<T>): T {
        return provideRetrofitBuilder(httpClientBuilder.provideOkHttpClient())
            .baseUrl(baseUrl)
            .build()
            .create(service)
    }

    private fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
    }
}