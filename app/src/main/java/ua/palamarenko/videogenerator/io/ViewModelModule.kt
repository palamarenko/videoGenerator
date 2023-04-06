package ua.palamarenko.videogenerator.io

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.palamarenko.videogenerator.ui.main.GenerateVideoViewModel


val viewModelModule = module {

    viewModel { GenerateVideoViewModel(get()) }

}



