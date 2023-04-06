package ua.palamarenko.videogenerator.io.video

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File

interface MainVideoGenerator {

    fun generateVideo(promt : String, duration : Long, progress : MutableStateFlow<ProgressState>) : Flow<File>
}