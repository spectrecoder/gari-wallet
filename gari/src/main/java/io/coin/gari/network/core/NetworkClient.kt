package io.coin.gari.network.core

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.coin.gari.exceptions.InvalidResponseBodyException
import io.coin.gari.network.Api
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.Type

internal class NetworkClient(
    private val okHttpClientProvider: OkHttpClientProvider,
    private val gson: Gson
) {

    private val httpClient = okHttpClientProvider.provideOkHttpClient()

    @Throws(InvalidResponseBodyException::class)
    fun <T> get(
        gariClientId: String,
        token: String,
        path: String,
        params: Map<String, String> = emptyMap(),
        responseClass: Class<T>
    ): T {
        val response = get(
            gariClientId,
            token,
            path,
            params
        )

        val responseBody = response.body?.string()

        if (responseBody.isNullOrEmpty()) {
            throw InvalidResponseBodyException()
        }

        val resultType: Type = TypeToken.getParameterized(responseClass).type
        return try {
            gson.fromJson(responseBody, resultType)
        } catch (error: Throwable) {
            throw InvalidResponseBodyException(error)
        }
    }

    fun get(
        gariClientId: String,
        token: String,
        path: String,
        params: Map<String, String> = emptyMap(),
    ): Response {
        val urlBuilder = Uri.parse(Api.Url.BASE_URL)
            .buildUpon()
            .path(path)

        for (eachParam in params) {
            urlBuilder.appendQueryParameter(eachParam.key, eachParam.value)
        }

        val url = urlBuilder.build()
            .toString()

        val request: Request = Request.Builder()
            .header(Api.Header.TOKEN, token)
            .header(Api.Header.GARI_CLIENT_ID, gariClientId)
            .url(url)
            .get()
            .build()

        return httpClient.newCall(request).execute()
    }

    fun setLogsEnabled(enable: Boolean) {
        okHttpClientProvider.setLogsEnabled(enable)
    }

    class Builder {

        fun build(): NetworkClient {
            return NetworkClient(
                okHttpClientProvider = OkHttpClientProvider(),
                gson = Gson()
            )
        }
    }
}