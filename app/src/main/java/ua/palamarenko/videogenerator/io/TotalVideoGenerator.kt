package ua.palamarenko.videogenerator.io

import android.os.Environment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ua.palamarenko.videogenerator.io.combiner.FullInfo
import ua.palamarenko.videogenerator.io.combiner.MainCombiner
import ua.palamarenko.videogenerator.io.text.gpt.GptTextGenerator
import ua.palamarenko.videogenerator.io.tts.freetts.FreeTTsVoiceGenerator
import ua.palamarenko.videogenerator.io.video.pexel.PexelVideoGenerator
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File

class TotalVideoGenerator(
    val voiceGenerator: FreeTTsVoiceGenerator,
    val textGenerator: GptTextGenerator,
    val videoGenerator: PexelVideoGenerator,
    val combiner: MainCombiner
) {


    fun generateVideo(promt: String, progress: MutableStateFlow<ProgressState>): Flow<File> {


        return textGenerator.generateHistory(promt, progress)
            .map { FullInfo(text = it.answer, tags = it.tags, promt = promt) }
            .flatMapLatest { info ->
                voiceGenerator.generateVoice(info.text!!, "en-US3", progress)
                    .map {
                        info.voice = it.first
                        info.duration = it.second
                        info
                    }
            }
            .flatMapLatest { info ->
                var promtVideo = ""
                info.tags!!.forEach {
                    promtVideo += "$it, "
                }

                videoGenerator.generateVideo(promtVideo, info.duration!!, progress)
                    .map {
                        info.video = it
                        info
                    }
            }
            .flatMapLatest { combiner.makeFullVideo(it, progress) }
    }
}