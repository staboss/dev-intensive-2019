package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt

/* KEYBOARD */

fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

fun Activity.isKeyboardClosed(): Boolean = !isKeyboardOpen()

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    getRootView().getWindowVisibleDisplayFrame(visibleBounds)

    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = convertDpToPx(50F).roundToInt()

    return heightDiff > marginOfError
}

/* OTHERS */
fun Activity.getRootView(): View = findViewById(android.R.id.content)

/* CONTEXT */

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.convertDpToPx(dp: Float): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
