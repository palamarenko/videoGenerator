package ua.palamarenko.videogenerator.io.combiner

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File


data class FullInfo(
    var text: String? = null,
    var voice: File? = null,
    var video: File? = null,
    var music: File? = null,
    var duration : Long? = null,
    var promt : String? = null,
    var tags : ArrayList<String>? = null
)

interface Combiner {
    fun makeFullVideo(info: FullInfo,  progress: MutableStateFlow<ProgressState>): Flow<File>
}