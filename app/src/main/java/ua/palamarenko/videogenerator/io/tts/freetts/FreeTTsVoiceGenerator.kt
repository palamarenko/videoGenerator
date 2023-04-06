package ua.palamarenko.videogenerator.io.tts.freetts

import android.graphics.Movie
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Base64
import com.google.android.exoplayer2.extractor.mp4.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ua.palamarenko.videogenerator.io.tts.MainVoiceGenerator
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class FreeTTsVoiceGenerator(val api : FreeTTsApi) : MainVoiceGenerator{


    override fun generateVoice(
        text: String,
        voiceID: String,
        progress: MutableStateFlow<ProgressState>
    ): Flow<Pair<File, Long>> {
       return  flow {
            progress.emit(ProgressState(0, "Voice generation start",true))
             val list =  generateList(text)
                .mapIndexed { index, s ->
                    val data = api.getVoice(body = TextToSpeechRequest(s,"servicebin",voiceID,"0")).audioData
                    saveBase64Mp3ToFile(data,index)!!
                }
           emit(list)
        }.map {
                combineAudioFiles(it,File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "voice3.mp3"
                ))
           }
           .map {
               progress.emit(ProgressState(100, "Voice generation finish",true))
               Pair(it,getMp3Duration(it))
           }
    }


    fun generateList(text : String) : List<String>{
        var startChar = 0
        val count = 500
        val list = ArrayList<String>()
        while (text.length > startChar){
            val end = if(startChar + count > text.length) text.length else startChar + count
            list.add(text.substring(startChar,end))
            startChar += count
        }
        return list
    }


    fun combineAudioFiles(audioFiles: List<File>, file: File) : File {
        val outputStream = FileOutputStream(file.absolutePath)

        for (file in audioFiles) {
            try {
                val fileStream = FileInputStream(file)
                val fileBytes = ByteArray(fileStream.available())
                fileStream.read(fileBytes)
                outputStream.write(fileBytes)
                fileStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        outputStream.flush()
        outputStream.close()

        return file
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



    fun saveBase64Mp3ToFile(base64AudioData: String, index : Int) : File? {
        try {
            // Decode Base64 audio data
            val decodedAudioData = Base64.decode(base64AudioData, Base64.DEFAULT)

            val audioFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "partVoice_${index}.mp3"
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


