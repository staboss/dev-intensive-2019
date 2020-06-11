package ru.skillbranch.devintensive.repositories

import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.App
//import ru.skillbranch.devintensive.models.Profile

object PreferencesRepository {

    private const val APP_THEME = "APP_THEME"

    private const val ABOUT = "ABOUT"
    private const val RATING = "RATING"
    private const val RESPECT = "RESPECT"
    private const val LAST_NAME = "LAST_NAME"
    private const val FIRST_NAME = "FIRST_NAME"
    private const val REPOSITORY = "REPOSITORY"

    private const val PREFERENCE_NAME = "DevSharedPreference"

    private val shPref = App.getAppContext()!!.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)

    fun saveAppTheme(theme: Int) {
        putValue(APP_THEME to theme)
    }

    fun getAppTheme(): Int {
        return shPref.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_NO)
    }

//    fun saveProfile(profile: Profile) {
//        with(profile) {
//            putValue(ABOUT to about)
//            putValue(RATING to rating)
//            putValue(RESPECT to respect)
//            putValue(LAST_NAME to lastName)
//            putValue(FIRST_NAME to firstName)
//            putValue(REPOSITORY to repository)
//        }
//    }
//
//    fun getProfile(): Profile = Profile(
//        about = shPref.getString(ABOUT, "")!!,
//        rating = shPref.getInt(RATING, 0),
//        respect = shPref.getInt(RESPECT, 0),
//        lastName = shPref.getString(LAST_NAME, "")!!,
//        firstName = shPref.getString(FIRST_NAME, "")!!,
//        repository = shPref.getString(REPOSITORY, "")!!
//    )

    private fun putValue(pair: Pair<String, Any>) = with(shPref.edit()) {
        val (key, value) = pair
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
        }
        apply()
    }
}