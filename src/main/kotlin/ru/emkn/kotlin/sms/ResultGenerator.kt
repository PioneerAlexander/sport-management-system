package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.time.LocalTime
import kotlin.math.max


fun generateResults(competition: Competition, outputPath: String = "comp"): List<List<String>> {
    val protocolGenerator = mutableListOf(listOf<String>())
    val participants = competition.participants
    val sortedByAgeGroup = participants.groupBy { it.ageGroup }

    csvWriter().open("$outputPath/results.csv") {
        writeRow(listOf("Протокол результатов.", "", "", "", "", "", "", "", "", ""))
        for (ageGroup in sortedByAgeGroup.keys) {
            var index = 0
            writeRow(ageGroup, "", "", "", "", "", "", "", "", "")
            protocolGenerator.add(listOf(ageGroup, "", "", "", "", "", "", "", "", ""))
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
            protocolGenerator.add(
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
            var winnerTime = LocalTime.of(0, 0, 0, 0)

            for (participant in sortedByAgeGroup[ageGroup]!! //только те, кто честно финишировал
                .filter { it.isNotCheated }
                .sortedBy { timeDifference(it.startTime, it.finishTime) }) {
                if (index == 0) {  //отдельно победителя записываем
                    winnerTime = timeDifference(participant.startTime, participant.finishTime)
                    participant.points = 100
                    writeRow(
                        listOf(
                            index + 1,
                            participant.startNumber,
                            participant.surname,
                            participant.name,
                            participant.birthYear,
                            participant.sportsCategory,
                            participant.organisation,
                            timeDifference(participant.startTime, participant.finishTime).forPrint(),
                            index + 1
                        )
                    )
                    protocolGenerator.add(
                        listOf(
                            (index + 1).toString(),
                            participant.startNumber,
                            participant.surname,
                            participant.name,
                            participant.birthYear,
                            participant.sportsCategory,
                            participant.organisation,
                            timeDifference(participant.startTime, participant.finishTime).forPrint(),
                            (index + 1).toString()
                        )
                    )
                } else {  //все честные кроме победителя
                    participant.points = max(
                        0,
                        (100 * (2.0 - ratioOfTwoTimes(
                            timeDifference(participant.startTime, participant.finishTime),
                            winnerTime
                        ))).toInt()
                    )
                    writeRow(
                        listOf(
                            index + 1,
                            participant.startNumber,
                            participant.surname,
                            participant.name,
                            participant.birthYear,
                            participant.sportsCategory,
                            participant.organisation,
                            timeDifference(participant.startTime, participant.finishTime).forPrint(),
                            index + 1,
                            "+${
                                timeDifference(
                                    winnerTime,
                                    timeDifference(participant.startTime, participant.finishTime)
                                ).forPrint()
                            }"
                        )
                    )
                    protocolGenerator.add(
                        listOf(
                            (index + 1).toString(),
                            participant.startNumber,
                            participant.surname,
                            participant.name,
                            participant.birthYear,
                            participant.sportsCategory,
                            participant.organisation,
                            timeDifference(participant.startTime, participant.finishTime).forPrint(),
                            (index + 1).toString(),
                            "+${
                                timeDifference(
                                    winnerTime,
                                    timeDifference(participant.startTime, participant.finishTime)
                                ).forPrint()
                            }"
                        )
                    )
                }
                index += 1
            }
            for (participant in sortedByAgeGroup[ageGroup]!! //кто нечестно финишировал
                .filter { !(it.isNotCheated) }) {
                writeRow(
                    listOf(
                        index + 1,
                        participant.startNumber,
                        participant.surname,
                        participant.name,
                        participant.birthYear,
                        participant.sportsCategory,
                        participant.organisation,
                        "снят"
                    )
                )
                protocolGenerator.add(listOf(
                    (index + 1).toString(),
                    participant.startNumber,
                    participant.surname,
                    participant.name,
                    participant.birthYear,
                    participant.sportsCategory,
                    participant.organisation,
                    "снят"
                ))
            }
        }
    }
    return protocolGenerator
}

fun generateTeamResults(
    competition: Competition,
    outputPath: String = "comp"
): List<List<String>> { //можно вызывать только после generateResults
    val sortedOrganisations =
        competition.orgs.sortedByDescending { org: Organisation -> org.members.sumOf { it.points } }
    val protocolGenerator = mutableListOf(listOf<String>())
    csvWriter().open("$outputPath/teamResults.csv") {
        writeRow(
            listOf(
                "№ п/п",
                "Название",
                "Место",
                "Результат",
            )
        )
        protocolGenerator.add(listOf(
            "№ п/п",
            "Название",
            "Место",
            "Результат",
        ))
        for ((index, organisation) in sortedOrganisations.withIndex()) {
            writeRow(
                listOf(
                    index + 1,
                    organisation.name,
                    index + 1,
                    organisation.members.sumOf {
                        it.points
                    }
                )
            )
            protocolGenerator.add(
                listOf(
                    (index + 1).toString(),
                    organisation.name,
                    (index + 1).toString(),
                    organisation.members.sumOf {
                        it.points
                    }.toString()
                )
            )
        }

    }
    return protocolGenerator
}

fun timeDifference(start: LocalTime, finish: LocalTime): LocalTime =
    finish.minusHours(start.hour.toLong())
        .minusMinutes(start.minute.toLong())
        .minusSeconds(start.second.toLong())

fun ratioOfTwoTimes(numerator: LocalTime, denominator: LocalTime): Double {
    return (3600 * numerator.hour + 60 * numerator.minute + numerator.second).toDouble() /
            (3600 * denominator.hour + 60 * denominator.minute + denominator.second)
}