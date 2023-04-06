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
        "you should answer only by json format like this - {message:message, tags[]} In message you must put exactly the story that you generated, story mast be with from 50 to 70 words (very important!!) message in query language, and in tags - tags to search for videos on this story, tags only in English. So the query itself is - "

    val test = GenerativeTextData(answer = "Відкриття пеніциліну - одне з найважливіших відкриттів в історії медицини. У 1928 році Олександр Флемінг помітив, що кілька петрівок з бактеріями, які він використовував у своїх експериментах, забув зачистити. На одній з них з'явився грибок, який виявився ефективним у боротьбі з бактеріями. Це було відкриттям пеніциліну. Ця речовина стала першим антибіотиком, який мав широке застосування в лікуванні інфекцій. Відкриття пеніциліну змінило медицину назавжди, дозволяючи лікарям лікувати раніше смертельні хвороби, такі як пневмонія, сепсис та інші інфекції. Воно стало ключовим для розвитку антибіотиків, які допомагають захищати нас від бактерій і інших патогенних мікроорганізмів.", tags = arrayListOf("socks", "lost and found", "puzzling"))

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