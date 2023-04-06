package ua.palamarenko.videogenerator.io.api.gpt

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface GptApi {



    @POST("/v1/chat/completions")
    suspend fun postGpt(@Header("Authorization") token: String = "Bearer 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", @Body body : ChatModel) : ChatCompletion
}




data class ChatModel(
    @SerializedName("model")
    val model: String,
    @SerializedName("messages")
    val messages: List<Message>
)

data class Message(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String
)


data class GenerativeTextData(@SerializedName("message")  val answer : String, @SerializedName("tags") val tags : ArrayList<String>)
data class Choice(
    @SerializedName("index")
    val index: Int,
    @SerializedName("message")
    val message: Message,
    @SerializedName("finish_reason")
    val finishReason: String
)

data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int
)

data class ChatCompletion(
    @SerializedName("id")
    val id: String,
    @SerializedName("object")
    val objectValue: String,
    @SerializedName("created")
    val created: Long,
    @SerializedName("choices")
    val choices: List<Choice>,
    @SerializedName("usage")
    val usage: Usage
)