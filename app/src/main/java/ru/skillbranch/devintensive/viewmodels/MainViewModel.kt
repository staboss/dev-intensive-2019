package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {

    private val chatRepository = ChatRepository

    private val query = mutableLiveData("")
    private val chatItems = Transformations.map(ChatRepository.loadChats()) { chats ->
        chats.asSequence()
            .filter { chat -> !chat.isArchived }
            .map { chat -> chat.toChatItem() }
            .sortedBy { chatItem -> chatItem.id.toInt() }
            .toList()
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryRex = query.value!!
            val chats = chatItems.value!!

            result.value = when {
                queryRex.isEmpty() -> chats
                else -> chats.filter { it.title.contains(queryRex, true) }
            }
        }

        result.addSource(chatItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}