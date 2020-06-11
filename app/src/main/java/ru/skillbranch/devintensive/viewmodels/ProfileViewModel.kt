package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel : ViewModel() {

    private val repository: PreferencesRepository = PreferencesRepository

    private val appTheme: MutableLiveData<Int> = MutableLiveData<Int>()
//    private val profileData: MutableLiveData<Profile> = MutableLiveData<Profile>()

    init {
//        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

//    fun getProfileData(): LiveData<Profile> = profileData

    fun getTheme(): LiveData<Int> = appTheme

//    fun saveProfileData(profile: Profile) {
//        repository.saveProfile(profile)
//        profileData.value = profile
//    }

    fun switchTeam() {
        appTheme.value = when (appTheme.value) {
            AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }
}