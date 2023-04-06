package ua.palamarenko.videogenerator.io.text.gpt

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ua.palamarenko.videogenerator.io.api.gpt.ChatModel
import ua.palamarenko.videogenerator.io.api.gpt.GenerativeTextData
import ua.palamarenko.videogenerator.io.api.gpt.GptApi
import ua.palamarenko.videogenerator.io.api.gpt.Message
import ua.palamarenko.videogenerator.io.text.MainTextGenerator
import ua.palamarenko.videogenerator.ui.ProgressState

class GptTextGenerator(val api: GptApi) : MainTextGenerator {

    val startMessage =
        "you should answer only by json format like this - {message:message, tags[]} In message you must put exactly the story that you generated, story mast be with from 120 to 150 words (very important!!) message in query language, and in tags - tags to search for videos on this story, tags only in English. So the query itself is - "

    val test = GenerativeTextData(answer = "Bipolar affective disorder (BAD) is a mental illness characterized by sudden fluctuations in mood and emotions in patients. People with BAD experience episodes of mania and depression, which can last from a few days to several months.\n" +
            "\n" +
            "Manic episodes are characterized by excessive activity, high energy, elevated mood, and aggressiveness. Individuals in a state of mania may engage in heavy drinking, drug use, or risky behaviors such as extreme sports or impulsive purchases.\n" +
            "\n" +
            "Depressive episodes are characterized by lowered mood, apathy, fatigue, and anxiety. People with BAD may experience suicidal thoughts and behavior.\n" +
            "\n" +
            "BAD is a chronic condition that requires long-term and thorough medication and psychotherapeutic therapy. Failure to comply with treatment can lead to worsening symptoms and serious consequences. While BAD is incurable, medication can help alleviate symptoms and improve the quality of life for people with this disorder.",
        tags = arrayListOf("mentalhealth", "bipolardisorder", "mentalillness", "psychology"))
    override fun generateHistory(promt: String, progress: MutableStateFlow<ProgressState>): Flow<GenerativeTextData> {
        return flow { emit(test) }
        return flow {
            progress.emit(ProgressState(0, "Text generation start",true))
            val resp = api.postGpt(
                body = ChatModel(
                    model = "gpt-3.5-turbo",
                    arrayListOf(Message(role = "user", content = "$startMessage $promt"))
                )
            )
            val message = resp.choices[0].message.content
            val data = Gson().fromJson<GenerativeTextData>(message, GenerativeTextData::class.java)
            progress.emit(ProgressState(100, "Text generation finish",true))
            emit(data)
        }.flowOn(Dispatchers.IO)
    }

}