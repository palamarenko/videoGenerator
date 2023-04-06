package ua.palamarenko.videogenerator.io.text

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.palamarenko.videogenerator.io.api.gpt.GenerativeTextData
import ua.palamarenko.videogenerator.ui.ProgressState

interface MainTextGenerator {

    fun generateHistory(promt: String, progress : MutableStateFlow<ProgressState>) : Flow<GenerativeTextData>
}