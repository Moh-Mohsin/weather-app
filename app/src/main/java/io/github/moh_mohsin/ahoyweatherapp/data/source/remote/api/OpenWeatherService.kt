package io.github.moh_mohsin.ahoyweatherapp.data.source.remote.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.github.moh_mohsin.ahoyweatherapp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class OpenWeatherService {

    fun getService(baseUrl: HttpUrl = BuildConfig.OPEN_WEATHER_BASE_URL.toHttpUrl()): OpenWeatherApi =
        getRetrofit(baseUrl).create(OpenWeatherApi::class.java)

    private fun getRetrofit(baseUrl: HttpUrl): Retrofit {

        val timeout = 60L
        val httpClient = OkHttpClient.Builder().apply {
            connectTimeout(timeout, TimeUnit.SECONDS)
            writeTimeout(timeout, TimeUnit.SECONDS)
            readTimeout(timeout, TimeUnit.SECONDS)

            // Log only on debug builds
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(logging)
            }

        }
        return Retrofit.Builder()
            .client(httpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .baseUrl(baseUrl)
            .build()
    }


    private fun getGson() =
        GsonBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

}


