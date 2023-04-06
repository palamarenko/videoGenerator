package ua.palamarenko.videogenerator.io.tts.freetts

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ua.palamarenko.videogenerator.io.api.pexel.PexelApi
import java.util.concurrent.TimeUnit

interface FreeTTsApi {

    @POST("/api/v1/tts")
    suspend fun getVoice(@Header("apikey") key : String = "v8nfcCBzjw7djyaEt0rPnwZzPsUN7aiq", @Body body: TextToSpeechRequest ) : TextToSpeechResponse
}

class FreeTTsRestClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: FreeTTsApi = Retrofit.Builder()
        .baseUrl("https://ttsfree.com/")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(client)
        .build()
        .create(FreeTTsApi::class.java)
}


data class TextToSpeechRequest(
    @SerializedName("text") val text: String,
    @SerializedName("voiceService")val voiceService: String,
    @SerializedName("voiceID")val voiceID: String,
    @SerializedName("voiceSpeed")val voiceSpeed: String
)

data class TextToSpeechResponse(
    @SerializedName("audioData") val audioData: String)
