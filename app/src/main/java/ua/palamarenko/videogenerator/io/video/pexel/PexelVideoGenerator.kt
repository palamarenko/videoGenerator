package ua.palamarenko.videogenerator.io.video.pexel

import VideoHandle.EpEditor
import VideoHandle.EpVideo
import VideoHandle.OnEditorListener
import android.content.Context
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import ua.palamarenko.videogenerator.io.api.pexel.PexelApi
import ua.palamarenko.videogenerator.io.api.pexel.Video
import ua.palamarenko.videogenerator.io.video.MainVideoGenerator
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalStateException

class PexelVideoGenerator(val context : Context, val api: PexelApi) : MainVideoGenerator {

    val videoDuration = 3000L


    override fun generateVideo(
        promt: String,
        duration: Long,
        progress: MutableStateFlow<ProgressState>
    ): Flow<File> {
        progress.tryEmit(ProgressState(0, "Start video generation", true))
        var currentDuration = 0L
        var videoNumber = 0

        return flow { emit(api.getVideos(query = promt)) }
            .flatMapLatest { data ->
                flow {
                    progress.tryEmit(ProgressState(3, "Get video response", true))

                    val allVideoCount = (duration / videoDuration).toInt() + 1

                    val list = data.videos.map {
                        if (currentDuration >= duration) {
                            return@map null
                        }
                        val currentVideoDuration =
                            if (duration - currentDuration > videoDuration) videoDuration
                            else duration - currentDuration

                        currentDuration += currentVideoDuration
                        videoNumber++

                        return@map saveAndCropVideo(
                            it,
                            currentVideoDuration,
                            progress,
                            allVideoCount,
                            videoNumber
                        ).firstOrNull()
                    }.filterNotNull()
                    emit(list)
                }

            }.flatMapLatest {
                progress.tryEmit(ProgressState(50, "Combine all video", true))
                plusVideo(it,progress) }
            .onEach {
                progress.tryEmit(ProgressState(100, "All Video combined", true))
            }
    }


    private fun saveAndCropVideo(
        video: Video,
        duration: Long,
        progress: MutableStateFlow<ProgressState>,
        allVideoCount : Int,
        currentVideo : Int
    ): Flow<File> {
        return flow {
            progress.tryEmit(ProgressState(0, "Video ${currentVideo}/${allVideoCount} start save", true))
            emit(
                saveVideo(video)?: throw IllegalStateException("Video generate problem")
            )

            progress.tryEmit(ProgressState(100, "Video ${currentVideo}/${allVideoCount} saved", true))
        }
            .flatMapLatest { cropVideo(it, duration) }.onEach {
                progress.tryEmit(ProgressState(100, "Video ${currentVideo}/${allVideoCount} croped", true))
            }
    }


    private fun plusVideo(files: List<File>,progressState: MutableStateFlow<ProgressState>): Flow<File> {
        val flow = MutableSharedFlow<File>(replay = 1)
        val result = File(
            context.filesDir,
            "result1.mp4"
        )
        val epVideos: ArrayList<EpVideo> = ArrayList()
        files.forEach {
            epVideos.add(EpVideo(it.absolutePath))
        }
        val outputOption = EpEditor.OutputOption(result.absolutePath)
        outputOption.frameRate = 30
        outputOption.bitRate = 10

        EpEditor.merge(epVideos, outputOption, object : OnEditorListener {
            override fun onSuccess() {
                flow.tryEmit(result)
            }

            override fun onFailure() {
                Log.d("HELLO", "ERROR")
            }

            override fun onProgress(progress: Float) {
                progressState.tryEmit(ProgressState((progress * 100).toInt(), "Combine all video", true))
                Log.d("HELLO", progress.toString())
            }

        })
        return flow
    }


    private fun cropVideo(file: File, duration: Long): Flow<File> {
        val flow = MutableSharedFlow<File>(replay = 1)
        val output = File(
            context.filesDir,
            "${file.nameWithoutExtension}_crop.mp4"
        )

        val epVideo = EpVideo(file.absolutePath)
        epVideo.clip(2f, duration.toFloat() / 1000)
        val outputOption = EpEditor.OutputOption(output.absolutePath)
        outputOption.frameRate = 30
        outputOption.bitRate = 10

        EpEditor.exec(epVideo, outputOption, object : OnEditorListener {
            override fun onSuccess() {
                flow.tryEmit(output)
            }

            override fun onFailure() {

            }

            override fun onProgress(progress: Float) {}

        })

        return flow
    }


    private suspend fun saveVideo(video: Video): File? {
        val link = video.videoFiles[0].link

        try {
            val response = api.downloadFile(link)

            val file = File(
                context.filesDir,
                "${video.id}.mp4"
            )
            response.byteStream().use { input ->
                FileOutputStream(file).use { output ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            return file

        } catch (e: IOException) {

        }

        return null

    }


}