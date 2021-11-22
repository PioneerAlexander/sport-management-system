package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

fun generateResultsFromSplits(participants: List<Participant>) {
    val outputPath = "testData/results.csv"
    val sortedByAgeGroup = participants.groupBy { it.ageGroup }

    csvWriter().open(outputPath) {
        writeRow(listOf("Протокол результатов.", "", "", "", "", "", "", "", "", ""))
        for (ageGroup in sortedByAgeGroup.keys) {
            writeRow("$ageGroup,,,,,,,,,")
            writeRow(
                listOf(
                    "№ п/п",
                    "Номер",
                    "Фамилия",
                    "Имя",
                    "Г.р.",
                    "Разр.",
                    "Команда",
                    "Результат",
                    "Место",
                    "Отставание"
                )
            )
            for ((index, participant) in participants.filter { it.isNotCheated() }.sortedBy { it.finishTime }
                .withIndex()) {
                writeRow(
                    listOf(
                        index,
                        participant.startNumber,
                        participant.surname,
                        participant.birthYear,
                        participant.sportsCategory,
                        participant.finishTime,
                        index,
                        participant.finishTime - TODO("Время победителя данной группы")
                    )
                )
            }
        }
    }
}
