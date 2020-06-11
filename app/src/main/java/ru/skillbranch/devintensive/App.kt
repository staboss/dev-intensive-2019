package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class App : Application() {

    companion object {

        private var context: Context? = null

        fun getAppContext() = context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        PreferencesRepository.getAppTheme().also {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}