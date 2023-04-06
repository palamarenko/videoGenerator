package ua.palamarenko.videogenerator

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ua.palamarenko.videogenerator.io.mainModule
import ua.palamarenko.videogenerator.io.viewModelModule




class App : Application() {


    override fun onCreate() {
        super.onCreate()
        initKoin()
    }




    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            printLogger(Level.ERROR)
            modules(mainModule, viewModelModule)
        }
    }

}

