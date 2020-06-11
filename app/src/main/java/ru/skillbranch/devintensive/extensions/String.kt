package ru.skillbranch.devintensive.extensions

val HTML_REGEX_1 = Regex("<.*?>")
val HTML_REGEX_2 = Regex("&.*?;")
val HTML_REGEX_3 = Regex("\\s+")

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

fun String.stripHtml(): String {
    var result = replace(HTML_REGEX_1, " ")
    result = result.replace(HTML_REGEX_2, " ")
    result = result.replace(HTML_REGEX_3, " ")
    return result.trim()
}

/* GITHUB URL VALIDATION */

fun String.isValidGithubUrl(): Boolean = matches(githubRegex)

val githubRegex by lazy {
    val regex = Regex("^(https://)?(www.)?(github.com/)(?!($githubExceptions)(?=/|$))(?![\\W])(?!\\w+[-]{2})[a-zA-Z0-9-]+(?<![-])(/)?$")
    regex
}

val githubExceptions by lazy {
    val exceptions = arrayOf(
        "enterprise", "features", "topics", "collections", "trending", "events", "join",
        "pricing", "nonprofit", "customer-stories", "security", "login", "marketplace"
    )
    exceptions.joinToString("|")
}
