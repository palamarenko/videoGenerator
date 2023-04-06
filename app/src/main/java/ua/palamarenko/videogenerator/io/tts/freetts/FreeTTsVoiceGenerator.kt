package ua.palamarenko.videogenerator.io.tts.freetts

import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Base64
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ua.palamarenko.videogenerator.io.tts.MainVoiceGenerator
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FreeTTsVoiceGenerator(val api : FreeTTsApi) : MainVoiceGenerator{



    override fun generateVoice(
        text: String,
        voiceID: String,
        progress: MutableStateFlow<ProgressState>
    ): Flow<Pair<File, Long>> {
       return  flow {
           progress.emit(ProgressState(0, "Voice generation start",true))
            emit(api.getVoice(body = TextToSpeechRequest(text,"servicebin",voiceID,"0")))
        }.map { saveBase64Mp3ToFile(it.audioData)!! }
           .map {
               progress.emit(ProgressState(100, "Voice generation finish",true))
               Pair(it,getMp3Duration(it))
           }
    }


    private fun getMp3Duration(file : File): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.absolutePath)
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationStr?.toLong() ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        } finally {
            retriever.release()
        }
    }



    fun saveBase64Mp3ToFile(base64AudioData: String) : File? {
        try {
            // Decode Base64 audio data
            val decodedAudioData = Base64.decode(base64AudioData, Base64.DEFAULT)

            val audioFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "voice1.mp3"
            )
            // Write the audio data to the file
            FileOutputStream(audioFile).use { fos ->
                fos.write(decodedAudioData)
            }

            return audioFile

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}


