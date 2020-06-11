package ru.skillbranch.devintensive.extensions

//import android.view.View
//import com.google.android.material.snackbar.Snackbar
//
//fun View.createSnackBar(
//    message: String,
//    textColor: Int? = null, textActionColor: Int? = null,
//    backgroundResource: Int? = null, duration: Int = Snackbar.LENGTH_SHORT,
//    actionText: String = "Отмена", action: (() -> Unit)? = null
//) = Snackbar.make(this, message, duration).apply {
//    textColor?.let { setTextColor(it) }
//    textActionColor?.let { setActionTextColor(it) }
//    backgroundResource?.let { this.view.setBackgroundResource(it) }
//    action?.let { setAction(actionText) { action.invoke() } }
//}