package ru.skillbranch.devintensive.utils

import android.content.Context
import java.util.*

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName.isNullOrBlank()) {
            return null to null
        }

        val values = fullName.trim().split("\\s+".toRegex())

        val firstName = values.getOrNull(0)
        val lastName = values.getOrNull(1)

        return firstName to lastName
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val firstLetter = firstName?.trim()?.firstOrNull()?.toUpperCase() ?: ""
        val secondLetter = lastName?.trim()?.firstOrNull()?.toUpperCase() ?: ""

        return "$firstLetter$secondLetter".ifEmpty { null }
    }

    fun transliteration(payload: String, divider: String = " "): String = payload
        .split("\\s+".toRegex()).joinToString(divider) { word ->
            word.map { letter ->
                when {
                    letter.toString() in transliteration -> transliteration[letter.toString()]
                    else -> letter
                }

            }.joinToString("")
        }
}

private val transliteration by lazy {
    transliterationLowerCase + transliterationUpperCase
}

private val transliterationLowerCase by lazy {
    mapOf(
        "а" to "a", "б" to "b", "в" to "v",
        "г" to "g", "д" to "d", "е" to "e",
        "ё" to "e", "ж" to "zh", "з" to "z",
        "и" to "i", "й" to "i", "к" to "k",
        "л" to "l", "м" to "m", "н" to "n",
        "о" to "o", "п" to "p", "р" to "r",
        "с" to "s", "т" to "t", "у" to "u",
        "ф" to "f", "х" to "h", "ц" to "c",
        "ч" to "ch", "ш" to "sh", "щ" to "sh'",
        "ъ" to "", "ы" to "i", "ь" to "",
        "э" to "e", "ю" to "yu", "я" to "ya"
    )
}

private val transliterationUpperCase by lazy {
    mutableMapOf<String, String>().apply {
        transliterationLowerCase.forEach { (k, v) ->
            if (v.length == 2) {
                this[k.toUpperCase(Locale("ru"))] = "${v[0].toUpperCase()}${v[1]}"
            } else {
                this[k.toUpperCase(Locale("ru"))] = v.toUpperCase(Locale("en"))
            }
        }
    }
}