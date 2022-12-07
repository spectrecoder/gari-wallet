package io.coin.gari.network.core

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.coin.gari.network.Api
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.Type

internal class NetworkClient(
    private val okHttpClientProvider: OkHttpClientProvider,
    private val gson: Gson
) {

    private val httpClient = okHttpClientProvider.provideOkHttpClient()

    fun <T> get(
        gariClientId: String,
        token: String,
        path: String,
        params: Map<String, String> = emptyMap(),
        response: Class<T>
    ): T {
        val urlBuilder = Uri.parse(Api.Url.BASE_URL)
            .buildUpon()
            .path(path)

        for (eachParam in params) {
            urlBuilder.appendQueryParameter(eachParam.key, eachParam.value)
        }

        val url = urlBuilder.build()
            .toString()

        val resultType: Type = TypeToken.getParameterized(response).type

        val request: Request = Request.Builder()
            .header(Api.Header.TOKEN, token)
            .header(Api.Header.GARI_CLIENT_ID, gariClientId)
            .url(url)
            .get()
            .build()

        val response: Response = httpClient.newCall(request).execute()
        return gson.fromJson(response.body!!.string(), resultType)
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