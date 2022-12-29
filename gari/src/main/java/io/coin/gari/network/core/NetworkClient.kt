package io.coin.gari.network.core

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.coin.gari.exceptions.InvalidResponseBodyException
import io.coin.gari.network.Api
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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
        responseType: Type
    ): T {
        return execute(
            request = {
                get(
                    gariClientId,
                    token,
                    path,
                    params
                )
            },
            responseType = { responseType }
        )
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

    @Throws(InvalidResponseBodyException::class)
    fun <T> post(
        gariClientId: String,
        token: String,
        path: String,
        params: Map<String, String> = emptyMap(),
        responseClass: Class<T>
    ): T {
        return post(
            gariClientId = gariClientId,
            token = token,
            path = path,
            params = params,
            responseType = TypeToken.getParameterized(responseClass).type
        )
    }

    @Throws(InvalidResponseBodyException::class)
    fun <T> post(
        gariClientId: String,
        token: String,
        path: String,
        params: Map<String, String> = emptyMap(),
        responseType: Type
    ): T {
        return execute(
            request = {
                post(
                    gariClientId,
                    token,
                    path,
                    params
                )
            },
            responseType = { responseType }
        )
    }

    fun post(
        gariClientId: String,
        token: String,
        path: String,
        params: Map<String, String> = emptyMap(),
    ): Response {
        val urlBuilder = Uri.parse(Api.Url.BASE_URL)
            .buildUpon()
            .path(path)

        val url = urlBuilder.build()
            .toString()

        val request: Request = Request.Builder()
            .header(Api.Header.TOKEN, token)
            .header(Api.Header.GARI_CLIENT_ID, gariClientId)
            .url(url)
            .post(gson.toJson(params).toRequestBody(JSON.toMediaType()))
            .build()

        return httpClient.newCall(request).execute()
    }

    fun setLogsEnabled(enable: Boolean) {
        okHttpClientProvider.setLogsEnabled(enable)
    }

    @Throws(InvalidResponseBodyException::class)
    private fun <T> execute(
        request: () -> Response,
        responseType: () -> Type
    ): T {
        val responseBody = request.invoke().body?.string()

        if (responseBody.isNullOrEmpty()) {
            throw InvalidResponseBodyException()
        }

        return try {
            gson.fromJson(responseBody, responseType.invoke())
        } catch (error: Throwable) {
            throw InvalidResponseBodyException(error)
        }
    }

    class Builder {

        fun build(): NetworkClient {
            return NetworkClient(
                okHttpClientProvider = OkHttpClientProvider(),
                gson = Gson()
            )
        }
    }

    private companion object {

        private const val JSON = "application/json; charset=utf-8"
    }
}