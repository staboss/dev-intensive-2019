package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> = when {
        question.answers.contains(answer) -> {
            question = question.nextQuestin()
            "Отлично – это правильный ответ!\n${question.question}" to status.color
        }
        else -> {
            status = status.nextStatus()
            "Это неправильный ответ!\n${question.question}" to status.color
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 255, 0));

        fun nextStatus(): Status = when {
            ordinal < values().lastIndex -> values()[ordinal + 1]
            else -> values().first()
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestin(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestin(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "iron", "wood", "metal")) {
            override fun nextQuestin(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestin(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestin(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", emptyList()) {
            override fun nextQuestin(): Question = IDLE
        };

        abstract fun nextQuestin(): Question
    }
}