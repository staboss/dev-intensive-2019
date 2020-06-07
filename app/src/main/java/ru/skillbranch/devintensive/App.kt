package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import net.alexandroid.shpref.ShPref
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class App : Application() {

    companion object {

        private var context: Context? = null

        fun getAppContext() = context
    }

    override fun onCreate() {
        super.onCreate()
        ShPref.init(this, ShPref.APPLY)

        PreferencesRepository.getAppTheme().also {
            AppCompatDelegate.setDefaultNightMode(it)
        }

        context = applicationContext
    }
}