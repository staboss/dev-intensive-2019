package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.models.data.toUserItem
import ru.skillbranch.devintensive.repositories.GroupRepository

class GroupViewModel : ViewModel() {

    private val groupRepository = GroupRepository

    private val query = mutableLiveData("")
    private val userItems = mutableLiveData(loadUsers())
    private val selectedItems = Transformations.map(userItems) { users ->
        users.filter { userItem -> userItem.isSelected }
    }

    fun getUsersData(): LiveData<List<UserItem>> {
        val result = MediatorLiveData<List<UserItem>>()

        val filterF = {
            val queryRex = query.value!!
            val users = userItems.value!!

            result.value = when {
                queryRex.isEmpty() -> users
                else -> users.filter { it.fullName.contains(queryRex, true) }
            }
        }

        result.addSource(userItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }


    fun getSelectedData(): LiveData<List<UserItem>> {
        return selectedItems
    }

    fun handleSelectedItems(userId: String) {
        userItems.value = userItems.value!!.map { userItem ->
            if (userItem.id == userId) {
                userItem.copy(isSelected = !userItem.isSelected)
            } else {
                userItem
            }
        }
    }

    fun handleRemoveChip(userId: String) {
        userItems.value = userItems.value!!.map { userItem ->
            if (userItem.id == userId) {
                userItem.copy(isSelected = false)
            } else {
                userItem
            }
        }
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }

    fun handleCreateGroup() {
        groupRepository.createChat(selectedItems.value!!)
    }

    private fun loadUsers(): List<UserItem> = groupRepository
        .loadUsers()
        .map { user -> user.toUserItem() }
}