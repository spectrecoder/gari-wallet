package io.gari.sample.network

import io.gari.sample.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class HttpClientBuilder {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(provideLoggingInterceptor())
        .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .build()

    fun provideOkHttpClient(): OkHttpClient {
        return okHttpClient
    }

    private fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    companion object {
        private const val OKHTTP_READ_TIMEOUT: Long = 60
        private const val OKHTTP_CONNECT_TIMEOUT = 30L
    }
}