package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.dpToPx(dp:Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.convertDpToPx(dp: Float): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

fun Context.getModeColor(attrColor: Int): Int {
    val value = TypedValue()
    theme.resolveAttribute(attrColor, value, true)
    return value.data
}