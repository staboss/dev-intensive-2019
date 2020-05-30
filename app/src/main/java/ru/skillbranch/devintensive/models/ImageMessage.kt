package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class ImageMessage(
    id: String, from: User?, chat: Chat, isIncoming: Boolean = false, date: Date = Date(),
    var image: String?
) : BaseMessage(id, from, chat, isIncoming, date) {

    override fun formatMessage(): String = buildString {
        append(from?.firstName + " ")
        append(if (isIncoming) "получил " else "отправил ")
        append("изображение \"$image\" ${date.humanizeDiff()}")
    }
}