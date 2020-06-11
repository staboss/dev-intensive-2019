package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

val LOCALE = Locale("ru")

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

const val STR_SECOND = "секунд"
const val STR_MINUTE = "минут"
const val STR_HOUR = "час"

fun Date.format(timeFormat: String = "HH:mm:ss dd.MM.yy"): String {
    var formatter: SimpleDateFormat

    return try {
        formatter = SimpleDateFormat(timeFormat, LOCALE)
        formatter.format(this)
    } catch (e: Exception) {
        formatter = SimpleDateFormat("HH:mm:ss dd.MM.yy", LOCALE)
        formatter.format(this)
    }
}

fun Date.shortFormat(): String? {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, LOCALE)
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time / DAY
    val day2 = date.time / DAY
    return day1 == day2
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date = apply {
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val isPast = time < date.time

    return when (val timeSeconds = abs(time - date.time)) {
        in 0..SECOND -> "только что"
        in SECOND..(45 * SECOND) -> when (isPast) {
            true -> "несколько секунд назад"
            else -> "через несколько секунд"
        }
        in (45 * SECOND)..(75 * SECOND) -> when (isPast) {
            true -> "минуту назад"
            else -> "через минуту"
        }
        in (75 * SECOND)..(45 * MINUTE) -> {
            val minutes = TimeUnits.MINUTE.plural((timeSeconds / MINUTE).toInt())
            when (isPast) {
                true -> "$minutes назад"
                else -> "через $minutes"
            }
        }
        in (45 * MINUTE)..(75 * MINUTE) -> when (isPast) {
            true -> "час назад"
            else -> "через час"
        }
        in (75 * MINUTE)..(22 * HOUR) -> {
            val hours = TimeUnits.HOUR.plural((timeSeconds / HOUR).toInt())
            when (isPast) {
                true -> "$hours назад"
                else -> "через $hours"
            }
        }
        in (22 * HOUR)..(26 * HOUR) -> when (isPast) {
            true -> "день назад"
            else -> "через день"
        }
        in (26 * HOUR)..(360 * DAY) -> {
            val days = TimeUnits.DAY.plural((timeSeconds / DAY).toInt())
            when (isPast) {
                true -> "$days назад"
                else -> "через $days"
            }
        }
        else -> when (isPast) {
            true -> "более года назад"
            else -> "более чем через год"
        }
    }
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value: Int): String {
        val format = when (this) {
            SECOND, MINUTE -> {
                val ending = when {
                    value.toString().last() in '2'..'4' && value !in 12..14 -> "ы"
                    value.toString().last() == '1' && value != 11 -> "у"
                    else -> ""
                }
                "${if (this == SECOND) STR_SECOND else STR_MINUTE}$ending"
            }
            HOUR -> {
                val ending = when {
                    value.toString().last() in '2'..'4' && value !in 12..14 -> "а"
                    value.toString().last() == '1' && value != 11 -> ""
                    else -> "ов"
                }
                "$STR_HOUR$ending"
            }
            DAY -> when {
                value.toString().last() in '2'..'4' && value !in 12..14 -> "дня"
                value.toString().last() == '1' && value != 11 -> "день"
                else -> "дней"
            }
        }

        return "$value $format"
    }
}