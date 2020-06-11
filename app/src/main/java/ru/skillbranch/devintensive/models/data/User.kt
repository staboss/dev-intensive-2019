package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    var lastVisit: Date? = Date(),
    var isOnline: Boolean = false
) {

    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(
        id = id,
        firstName = null,
        lastName = null
    )

    constructor(builder: Builder) : this(
        id = builder.id,
        firstName = builder.firstName,
        lastName = builder.lastName,
        avatar = builder.avatar,
        rating = builder.rating,
        respect = builder.respect,
        lastVisit = builder.lastVisit,
        isOnline = builder.isOnline
    )

    class Builder {

        var id: String = ""
        var firstName: String? = null
        var lastName: String? = null
        var avatar: String? = null
        var rating: Int = 0
        var respect: Int = 0
        var lastVisit: Date? = null
        var isOnline: Boolean = false

        fun id(id: String): Builder = apply {
            this.id = id
        }

        fun firstName(firstName: String?): Builder = apply {
            this.firstName = firstName
        }

        fun lastName(lastName: String?): Builder = apply {
            this.lastName = lastName
        }

        fun avatar(avatar: String?): Builder = apply {
            this.avatar = avatar
        }

        fun rating(rating: Int): Builder = apply {
            this.rating = rating
        }

        fun respect(respect: Int): Builder = apply {
            this.respect = respect
        }

        fun lastVisit(lastVisit: Date? = Date()): Builder = apply {
            this.lastVisit = lastVisit
        }

        fun isOnline(isOnline: Boolean): Builder = apply {
            this.isOnline = isOnline
        }

        fun build(): User {
            if (lastVisit == null) {
                lastVisit = Date()
            }
            return User(this)
        }
    }

    companion object Factory {

        private var lastId = -1

        fun makeUser(fullName: String?): User {
            lastId++

            // деструктаризация объектов
            val (firstName, lastName) = Utils.parseFullName(fullName)

            return User(
                id = "$lastId",
                firstName = firstName,
                lastName = lastName
            )
        }

        fun resetIdIndex() {
            lastId = -1
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        return result
    }
}

fun User.toUserItem(): UserItem {
    val lastActivity = when {
        lastVisit == null -> "Еще ни разу не заходил"
        isOnline -> "online"
        else -> "Последний раз был ${lastVisit!!.humanizeDiff()}"
    }

    return UserItem(
        id = id,
        fullName = "${firstName.orEmpty()} ${lastName.orEmpty()}",
        initials = Utils.toInitials(firstName, lastName),
        avatar = avatar,
        lastActivity = lastActivity,
        isSelected = false,
        isOnline = isOnline
    )
}