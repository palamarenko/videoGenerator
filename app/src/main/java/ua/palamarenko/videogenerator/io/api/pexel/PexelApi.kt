package ua.palamarenko.videogenerator.io.api.pexel

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url


interface PexelApi {
    @GET("/videos/search")
    suspend fun getVideos(@Header("Authorization") Authorization : String = "GUrFPaiiJ29bS2cEiX0tXCjzlqGhdfaST1Ye9bqsn2hNGW486Dp8v5Zo", @Query("query") query : String) : VideoData

    @GET
    @Streaming
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody
}


data class VideoData(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("url") val url: String,
    @SerializedName("videos") val videos: List<Video>
)

data class Video(
    @SerializedName("id") val id: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("url") val url: String,
    @SerializedName("image") val image: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("user") val user: User,
    @SerializedName("video_files") val videoFiles: List<VideoFile>,
    @SerializedName("video_pictures") val videoPictures: List<VideoPicture>
)

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class VideoFile(
    @SerializedName("id") val id: Int,
    @SerializedName("quality") val quality: String,
    @SerializedName("file_type") val fileType: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("link") val link: String
)

data class VideoPicture(
    @SerializedName("id") val id: Int,
    @SerializedName("picture") val picture: String,
    @SerializedName("nr") val nr: Int
)