package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.LocalDate
import java.time.LocalTime

fun saveCompetition(folderName: String = "comp", competition: Competition) {
    csvWriter().open("$folderName/data.csv") {
        writeRow(competition.name, competition.date)
        writeRow(
            listOf(
                "Фамилия",
                "Имя",
                "Г.р.",
                "Группа",
                "Разряд",
                "Команда",
                "Номер",
                "Час",
                "Минута",
                "Секунда",
            )
        )
        for (participant in competition.participants) {
            writeRow(participant.toList())
        }
    }
}

fun recreateSavedCompetition(folderPath: String = "comp"): Competition {
    var eventName = ""
    var eventDate = LocalDate.parse("2021-11-21")
    val allParticipants = mutableListOf<Participant>()
    csvReader().open("$folderPath/data.csv") {
        val listedNameDate = readNext()
        require(listedNameDate != null) { "damaged data file" }
        require(listedNameDate.isNotEmpty()) { "damaged data file" }
        eventName = listedNameDate[0]
        eventDate = LocalDate.parse(listedNameDate[1])
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            val currentParticipant = Participant(
                checkMapElement(row["Группа"]),
                checkMapElement(row["Фамилия"]),
                checkMapElement(row["Имя"]),
                checkMapElement(row["Г.р."]),
                checkMapElement(row["Разряд"]),
                checkMapElement(row["Команда"]).trim()
            )
            currentParticipant.startNumber = checkMapElement(row["Номер"])
            currentParticipant.startTime =
                LocalTime.of(
                    checkMapElement(row["Час"]).toInt(),
                    checkMapElement(row["Минута"]).toInt(),
                    checkMapElement(row["Секунда"]).toInt()
                )
            allParticipants.add(currentParticipant)
        }
    }

    val mappedParticipants = allParticipants.groupBy { it.organisation }
    val organisations = mappedParticipants.map { Organisation(it.key, it.value) }

    return Competition(eventName, eventDate, organisations)
}
