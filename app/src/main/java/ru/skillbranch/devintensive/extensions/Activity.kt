package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import kotlin.math.roundToInt

fun Activity.getRootView(): View = findViewById(android.R.id.content)

fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

fun Activity.isKeyboardClosed(): Boolean = !isKeyboardOpen()

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    getRootView().getWindowVisibleDisplayFrame(visibleBounds)

    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = convertDpToPx(50F).roundToInt()

    return heightDiff > marginOfError
}

fun Activity.getModeColor(attrColor: Int): Int {
    val value = TypedValue()
    theme.resolveAttribute(attrColor, value, true)
    return value.data
}