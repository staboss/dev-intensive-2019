package ru.skillbranch.devintensive.repositories

import androidx.appcompat.app.AppCompatDelegate
import net.alexandroid.shpref.ShPref
import ru.skillbranch.devintensive.models.Profile

object PreferencesRepository {

    private const val APP_THEME = "APP_THEME"

    private const val ABOUT = "ABOUT"
    private const val RATING = "RATING"
    private const val RESPECT = "RESPECT"
    private const val LAST_NAME = "LAST_NAME"
    private const val FIRST_NAME = "FIRST_NAME"
    private const val REPOSITORY = "REPOSITORY"

    fun saveAppTheme(theme: Int) {
        putValue(APP_THEME to theme)
    }

    fun getAppTheme(): Int {
        return ShPref.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun saveProfile(profile: Profile) {
        with(profile) {
            putValue(ABOUT to about)
            putValue(RATING to rating)
            putValue(RESPECT to respect)
            putValue(LAST_NAME to lastName)
            putValue(FIRST_NAME to firstName)
            putValue(REPOSITORY to repository)
        }
    }

    fun getProfile(): Profile = Profile(
        about = ShPref.getString(ABOUT, ""),
        rating = ShPref.getInt(RATING, 0),
        respect = ShPref.getInt(RESPECT, 0),
        lastName = ShPref.getString(LAST_NAME, ""),
        firstName = ShPref.getString(FIRST_NAME, ""),
        repository = ShPref.getString(REPOSITORY, "")
    )

    private fun putValue(pair: Pair<String, Any>) {
        val (key, value) = pair
        ShPref.put(key, value)
    }
}