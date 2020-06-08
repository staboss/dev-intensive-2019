package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    var isEditMode = false

    lateinit var viewFields: Map<String, TextView>
    private lateinit var viewModel: ProfileViewModel

    private val viewModelFactory = AndroidViewModelFactory.getInstance(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        // setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Log.d("M_ProfileActivity", "onCreate")

        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = viewModelFactory.create(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "update theme")
        delegate.localNightMode = mode
        // AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also { profileData ->
            for ((key, view) in viewFields) {
                view.text = profileData[key].toString()
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {
        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false

        viewFields = mapOf(
            "rank" to tv_rank, "nickName" to tv_nick_name,
            "rating" to tv_rating, "respect" to tv_respect,
            "about" to et_about, "repository" to et_repository,
            "lastName" to et_last_name, "firstName" to et_first_name
        )

        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)

            if (!isEditMode) {
                saveProfileInfo()
            }
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTeam()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter {
            it.key in setOf("about", "lastName", "firstName", "repository")
        }

        info.forEach { (_, view) ->
            view as EditText
            with(view) {
                isEnabled = isEdit
                isFocusable = isEdit
                isFocusableInTouchMode = isEdit
                background.alpha = if (isEdit) 255 else 0
            }
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon: Drawable = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile(
            about = et_about.text.toString(),
            lastName = et_last_name.text.toString(),
            firstName = et_first_name.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }
}