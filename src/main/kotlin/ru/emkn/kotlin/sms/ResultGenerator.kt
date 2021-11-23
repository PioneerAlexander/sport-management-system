package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.time.LocalTime

fun finishTimeToParticipant(participant: Participant) {
    if (participant.startNumber in Participant.mapFromNumberToSplits.keys) {
        participant.finishTime = Participant.mapFromNumberToSplits[participant.startNumber]!!.last().time
    }
}

fun generateResultsFromSplits(participants: List<Participant>, outputPath:String) {
    val sortedByAgeGroup = participants.groupBy { it.ageGroup }

    csvWriter().open(outputPath) {
        writeRow(listOf("Протокол результатов.", "", "", "", "", "", "", "", "", ""))
        for (ageGroup in sortedByAgeGroup.keys) {
            writeRow(ageGroup,"","","","","","","","","")
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
            for (participant in participants) {
                finishTimeToParticipant(participant)
            }
            for ((index, participant) in sortedByAgeGroup[ageGroup]!!
                .filter { it.isNotCheated() }
                .sortedBy { timeDifference(it.startTime, it.finishTime) }
                .withIndex()) {
                var winnerTime = LocalTime.of(0, 0, 0, 0)
                if (index == 0) {
                    winnerTime = participant.finishTime
                    writeRow(
                        listOf(
                            index+1,
                            participant.startNumber,
                            participant.surname,
                            participant.name,
                            participant.birthYear,
                            participant.sportsCategory,
                            participant.organisation,
                            timeDifference(participant.startTime, participant.finishTime).forPrint(),
                            index+1
                        )
                    )
                } else {
                    writeRow(
                        listOf(
                            index+1,
                            participant.startNumber,
                            participant.surname,
                            participant.name,
                            participant.birthYear,
                            participant.sportsCategory,
                            participant.organisation,
                            timeDifference(participant.startTime, participant.finishTime).forPrint(),
                            index+1,
                            "+${timeDifference(winnerTime, timeDifference(participant.startTime,participant.finishTime))}"
                        )
                    )
                }
            }
        }
    }
}

fun timeDifference(start: LocalTime, finish: LocalTime): LocalTime =
    finish.minusHours(start.hour.toLong())
        .minusMinutes(start.minute.toLong())
        .minusSeconds(start.second.toLong())