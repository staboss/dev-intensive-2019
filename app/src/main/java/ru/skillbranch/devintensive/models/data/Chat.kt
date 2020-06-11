package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int = messages.count { !it.isRead }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? = messages.lastOrNull()?.date

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String?, String?> =
        when (val lastMessage = messages.lastOrNull()) {
            is TextMessage -> lastMessage.text to lastMessage.from.firstName
            is ImageMessage -> "${lastMessage.from.firstName} - отправил фото" to "${lastMessage.from.firstName}"
            else -> "Сообщений еще нет" to ""
        }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id = id,
                avatar = user.avatar,
                initials = Utils.toInitials(user.firstName, user.lastName) ?: "??",
                title = "${user.firstName ?: ""} ${user.lastName ?: ""}",
                shortDescription = lastMessageShort().first,
                messageCount = unreadableMessageCount(),
                lastMessageDate = lastMessageDate()?.shortFormat(),
                isOnline = user.isOnline
            )
        } else {
            ChatItem(
                id = id,
                avatar = null,
                initials = "",
                title = title,
                shortDescription = lastMessageShort().first,
                messageCount = unreadableMessageCount(),
                lastMessageDate = lastMessageDate()?.shortFormat(),
                isOnline = false,
                chatType = ChatType.GROUP,
                author = lastMessageShort().second
            )
        }
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}
