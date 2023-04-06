package ua.palamarenko.videogenerator.ui

import android.content.Context

object ContextProvider {
    lateinit var applicationContext: Context

    fun init(context: Context) {
        this.applicationContext = context
    }

}




