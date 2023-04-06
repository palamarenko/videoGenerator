package ua.palamarenko.videogenerator.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ua.palamarenko.videogenerator.io.TotalVideoGenerator
import ua.palamarenko.videogenerator.io.combiner.FullInfo
import ua.palamarenko.videogenerator.io.combiner.MainCombiner
import ua.palamarenko.videogenerator.io.text.gpt.GptTextGenerator
import ua.palamarenko.videogenerator.io.tts.freetts.FreeTTsVoiceGenerator
import ua.palamarenko.videogenerator.io.video.pexel.PexelVideoGenerator
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File
import java.util.*





class GenerateVideoViewModel(val videoGenerator : TotalVideoGenerator) : ViewModel() {

    val state = MutableStateFlow("1")

    val shared = MutableStateFlow<File?>(null)

    val progress = MutableStateFlow<ProgressState>(ProgressState(0,""))

    fun generateVideo(promt : String){

        videoGenerator.generateVideo(promt,progress)
            .onEach {
                shared.tryEmit(it)
            }.launchIn(viewModelScope)
    }

}