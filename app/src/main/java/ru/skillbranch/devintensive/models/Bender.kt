package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    private var counter = 0

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> = when {
        question == Question.IDLE -> {
            "Отлично - ты справился\nНа этом все, вопросов больше нет" to status.color
        }
        !question.validate(answer) -> {
            "${question.rule}\n${question.question}" to status.color
        }
        question.answers.contains(answer) -> {
            counter = 0
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        }
        else -> {
            counter++
            status = status.nextStatus()

            when (counter) {
                3 -> "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                else -> "Это неправильный ответ\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status = when {
            ordinal < values().lastIndex -> values()[ordinal + 1]
            else -> values().first()
        }
    }

    enum class Question(val question: String, val answers: List<String>) {

        NAME("Как меня зовут?", listOf("Бендер", "bender")) {
            override val rule = "Имя должно начинаться с заглавной буквы"

            override fun nextQuestion(): Question = PROFESSION

            override fun validate(answer: String): Boolean = answer.first().isUpperCase()
        },

        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {

            override val rule: String = "Профессия должна начинаться со строчной буквы"

            override fun nextQuestion(): Question = MATERIAL

            override fun validate(answer: String): Boolean = answer.first().isLowerCase()
        },

        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {

            override val rule: String = "Материал не должен содержать цифр"

            override fun nextQuestion(): Question = BDAY

            override fun validate(answer: String): Boolean = answer.all { it !in '0'..'9' }
        },

        BDAY("Когда меня создали?", listOf("2993")) {

            override val rule: String = "Год моего рождения должен содержать только цифры"

            override fun nextQuestion(): Question = SERIAL

            override fun validate(answer: String): Boolean = answer.all { it in '0'..'9' }
        },

        SERIAL("Мой серийный номер?", listOf("2716057")) {

            override val rule: String = "Серийный номер содержит только цифры, и их 7"

            override fun nextQuestion(): Question = IDLE

            override fun validate(answer: String): Boolean =
                answer.all { it in '0'..'9' } && answer.length == 7
        },

        IDLE("На этом все, вопросов больше нет", emptyList()) {

            override val rule: String = ""

            override fun nextQuestion(): Question = IDLE

            override fun validate(answer: String): Boolean = true
        };

        abstract val rule: String

        abstract fun nextQuestion(): Question

        abstract fun validate(answer: String): Boolean
    }
}