package ru.skillbranch.devintensive.utils

import java.util.*

object Utils {

    /**
     * Утилитный метод [parseFullName]
     *
     * @param fullName полное имя пользователя [fullName]
     * @return пара значений [Pair] (firstName, lastName)
     */
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName.isNullOrBlank()) {
            return null to null
        }

        val values = fullName.trim().split("\\s+".toRegex())

        val firstName = values.getOrNull(0)
        val lastName = values.getOrNull(1)

        return firstName to lastName
    }

    /**
     * Утилитный метод [toInitials]
     *
     * @param firstName имя пользователя
     * @param lastName фамилия пользователя
     * @return строка с первыми буквами имени и фамилии в верхнем регистре
     */
    fun toInitials(firstName: String?, lastName: String?): String? {
        if (!firstName.isNullOrBlank() && !lastName.isNullOrBlank()) {
            return "${firstName[0].toUpperCase()}${lastName[0].toUpperCase()}"
        }

        if (firstName.isNullOrBlank()) {
            if (!lastName.isNullOrBlank() && 0 in lastName.indices) {
                return lastName[0].toUpperCase().toString()
            }
        }

        if (lastName.isNullOrBlank()) {
            if (!firstName.isNullOrBlank() && 0 in firstName.indices) {
                return firstName[0].toUpperCase().toString()
            }
        }

        return null
    }

    /**
     * Утилитный метод [transliteration]
     *
     * @param payload строка
     * @param [divider] разделитель
     * @return преобразованная строка из латинских символов
     */
    fun transliteration(payload: String, divider: String = " "): String = payload
        .split("\\s+".toRegex()).joinToString(divider) { word ->
            word.map { letter ->
                when {
                    letter.toString() in transliteration -> transliteration[letter.toString()]
                    else -> letter
                }

            }.joinToString("")
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
}
