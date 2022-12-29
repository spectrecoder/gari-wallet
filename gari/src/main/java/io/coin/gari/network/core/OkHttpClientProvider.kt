package io.coin.gari.network.core

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

internal class OkHttpClientProvider {

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(getLogLevel(LOGS_ENABLED_DEFAULT))

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS).build()

    fun setLogsEnabled(enable: Boolean) {
        loggingInterceptor.setLevel(getLogLevel(enable))
    }

    fun provideOkHttpClient(): OkHttpClient {
        return okHttpClient
    }

    private fun getLogLevel(enable: Boolean): HttpLoggingInterceptor.Level {
        return if (enable) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    companion object {
        private const val OKHTTP_READ_TIMEOUT: Long = 60
        private const val OKHTTP_CONNECT_TIMEOUT = 30L
        private const val LOGS_ENABLED_DEFAULT = false
    }
}