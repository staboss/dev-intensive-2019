package ru.skillbranch.devintensive.extensions


val HTML_REGEX_1 = Regex("<.*?>")
val HTML_REGEX_2 = Regex("&.*?;")
val HTML_REGEX_3 = Regex("\\s+")

/**
 * Усекает исходную строку до указанного числа символов [value]
 *
 * @param value количество символов
 * @return усеченная строка с заполнителем "..."
 */
fun String.truncate(value: Int = 16): String {
    if (isBlank() || isEmpty() || value == 0) {
        return ""
    }

    var result = trim().replace("\\s+".toRegex(), " ")

    if (result.length < value) {
        while (result.last().isWhitespace()) {
            result = result.substring(0, result.lastIndex)
        }
        return result
    }

    result = result.substring(0, value)
    while (result.last().isWhitespace()) {
        result = result.substring(0, result.lastIndex)
    }

    return "$result..."
}

/**
 * Очищает строку от html тегов и html escape последовательностей
 *
 * @return модифицированная строка
 */
fun String.stripHtml(): String {
    var result = replace(HTML_REGEX_1, " ")
    result = result.replace(HTML_REGEX_2, " ")
    result = result.replace(HTML_REGEX_3, " ")
    return result.trim()
}