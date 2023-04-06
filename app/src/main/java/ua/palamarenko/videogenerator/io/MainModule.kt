package ua.palamarenko.videogenerator.io

import org.koin.dsl.module
import ua.palamarenko.videogenerator.io.api.gpt.GptRestClient
import ua.palamarenko.videogenerator.io.api.pexel.PexelsRestClient
import ua.palamarenko.videogenerator.io.combiner.MainCombiner
import ua.palamarenko.videogenerator.io.text.gpt.GptTextGenerator
import ua.palamarenko.videogenerator.io.tts.freetts.FreeTTsRestClient
import ua.palamarenko.videogenerator.io.tts.freetts.FreeTTsVoiceGenerator
import ua.palamarenko.videogenerator.io.video.pexel.PexelVideoGenerator


val mainModule = module {

    single {
        val client = PexelsRestClient()
        client.api
    }

    single {
        val client = GptRestClient()
        client.api
    }

    single {
        val client = FreeTTsRestClient()
        client.api
    }

//    single {
//        AndroidVoiceGenerator(get())
//    }

    single {
        FreeTTsVoiceGenerator(get())
    }

    single {
        TotalVideoGenerator(get(),get(),get(),get())
    }


    single {
        GptTextGenerator(get())
    }

    single {
        PexelVideoGenerator(get(),get())
    }

    single {
        MainCombiner(get())
    }
}