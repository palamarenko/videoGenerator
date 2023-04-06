package ua.palamarenko.videogenerator.io.tts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File

interface MainVoiceGenerator {

    fun generateVoice(text : String, voiceID : String, progress : MutableStateFlow<ProgressState>) : Flow<Pair<File,Long>>
}