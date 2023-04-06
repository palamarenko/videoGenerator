package ua.palamarenko.videogenerator.io.combiner

import VideoHandle.EpEditor
import VideoHandle.EpVideo
import VideoHandle.OnEditorListener
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import ua.palamarenko.videogenerator.ui.ProgressState
import java.io.File

class MainCombiner : Combiner {


    override fun makeFullVideo(info: FullInfo,  progressFlow: MutableStateFlow<ProgressState>): Flow<File> {
        val flow = MutableSharedFlow<File>(replay = 1)
        val result = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "final1.mp4"
        )

        progressFlow.tryEmit(ProgressState(0, "Adding voice to video", true))

        EpEditor.music(info.video!!.absolutePath, info.voice!!.absolutePath, result.absolutePath, 1f, 1f, object : OnEditorListener {
            override fun onSuccess() {
                progressFlow.tryEmit(ProgressState(100, "Video done", false))
                flow.tryEmit(result)
            }

            override fun onFailure() {
                Log.d("HELLO","ERROR")

            }

            override fun onProgress(progress: Float) {
                progressFlow.tryEmit(ProgressState((progress * 100).toInt(), "Adding voice to video", true))
            }

        })
        return flow
    }



}