package ua.palamarenko.videogenerator.io.tts.android

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.palamarenko.videogenerator.io.tts.MainVoiceGenerator
import java.io.File
import java.util.*

//class AndroidVoiceGenerator(val context: Context) : MainVoiceGenerator,TextToSpeech.OnInitListener {
//
//    private var textToSpeech: TextToSpeech = TextToSpeech(context, this)
//    private var stateFlow = MutableSharedFlow<File>(replay = 1)
//    private var currentLocale : Locale = Locale.getDefault()
//    private var lastMessage : String = ""
//
//    private val file = File(
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//        "voice.wav"
//    )
//
//    override fun onInit(status: Int) {
//        if (status == TextToSpeech.SUCCESS) {
//            val result = textToSpeech.setLanguage(currentLocale)
//            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
//                val mostRecentUtteranceID = UUID.randomUUID().toString() // "" is String force
//                val params = Bundle()
//                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, mostRecentUtteranceID)
//
//                textToSpeech.synthesizeToFile(
//                    lastMessage,
//                    null,
//                    file,
//                    "voice"
//                )
//
//                textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
//                    override fun onDone(utteranceId: String) {
//                        stateFlow.tryEmit(file)
//                    }
//
//                    override fun onError(utteranceId: String) {
//                        Log.d("HELLO",utteranceId)
//                    }
//
//                    override fun onStart(utteranceId: String) {
//                        Log.d("HELLO",utteranceId)
//                    }
//                })
//
//            }
//        }
//    }
//
//
//    private fun generateFile(){
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    override fun generateVoice(text: String, locale: Locale): Flow<Pair<File, Long>> {
//        currentLocale = locale
//        lastMessage = text
//        return stateFlow
//
//    }
//}

