package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {

    /**
     * Абстрактный метод [formatMessage]
     *
     * @return возвращает строку содержащюю информацию:
     *  - [id] сообщения
     *  - имени получателя/отправителя [from]
     *  - виде сообщения ("получил/отправил") [isIncoming]
     *  - типе сообщения ("сообщение"/"изображение")
     */
    abstract fun formatMessage(): String

    /**
     * Паттерн [AbstractFactory] для класса [BaseMessage]
     */
    companion object AbstractFactory {

        private var lastId = -1

        /**
         * Метод [makeMessage] создает новое сообщение
         *
         * @param from пользователь создавший сообщение
         * @param chat чат к которому относится сообщение
         * @param date дата сообщения
         * @param type тип сообщения ("text/image")
         * @param payload полезная нагрузка
         * @return экземпляр класса [BaseMessage]
         */
        fun makeMessage(
            from: User?, chat: Chat, date: Date = Date(),
            type: String, payload: Any?, isIncoming: Boolean = false
        ): BaseMessage {

            lastId++

            return when (type) {
                "image" -> {
                    ImageMessage(
                        id = "$lastId",
                        from = from,
                        chat = chat,
                        date = date,
                        image = payload as? String,
                        isIncoming = isIncoming
                    )
                }
                else -> {
                    TextMessage(
                        id = "$lastId",
                        from = from,
                        chat = chat,
                        date = date,
                        text = payload as? String,
                        isIncoming = isIncoming
                    )
                }
            }
        }
    }
}