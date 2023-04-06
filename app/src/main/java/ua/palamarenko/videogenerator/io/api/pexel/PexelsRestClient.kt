package ua.palamarenko.videogenerator.io.api.pexel

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PexelsRestClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: PexelApi = Retrofit.Builder()
        .baseUrl("https://api.pexels.com/")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(client)
        .build()
        .create(PexelApi::class.java)
}