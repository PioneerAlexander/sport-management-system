package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import kotlinx.datetime.*


fun applicationToOrg(fileName: String): Organisation {
    //check how kotlin-csv works https://github.com/doyaaaaaken/kotlin-csv
    val org = Organisation()
    csvReader().open(fileName) {
        org.name = readNext()!![0]
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            org.addMember(Participant(row["Группа"], row["Фамилия"], row["Имя"], row["Г.р."], row["Разр."]))
        }
    }
    return org
}

fun competitionToStartLists(comp: Competition) {
    val mappedParticipants = comp.participants.groupBy { it.ageGroup }
    val numberLength = comp.size.toString().length
    var startNumber = 0
    var time = LocalDateTime(
        2021, 11, 21,
        12, 0, 0, 0
    ).toInstant(TimeZone.UTC)
    for ((index, category) in mappedParticipants.keys.withIndex()) {
        csvWriter().open("testData/testStartProtocol$index.csv") {
            writeRow(
                listOf(category, "", "", "", "", "")
            )
            for (participant in mappedParticipants[category]!!.shuffled()) {
                var participantNumber = startNumber.toString()
                while (participantNumber.length < numberLength) {
                    participantNumber = "0$participantNumber"
                }
                writeRow(
                    listOf(
                        participantNumber, participant.surname, participant.name,
                        participant.birthYear, participant.sportsCategory, time.toString().substring(11..18)
                    )
                )
                time = time.plus(DateTimePeriod(minutes = 1), TimeZone.UTC)
                startNumber += 1
            }
        }
    }
}

fun makeCompetition(pathEvent: String): Competition {
    var eventName = ""
    var eventDate = ""  //maybe kotlinx-datetime
    csvReader().open(pathEvent) {
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            //check for only 1 element
            eventName = row["Название"]!!
            eventDate = row["Дата"]!!
        }
    }
    return Competition(eventName, eventDate)
}


fun main(args: Array<String>) {
    val pathEvent = "sample-data/event.csv"
    val pathApplications = "sample-data/applications"
    val comp = makeCompetition(pathEvent)
    comp.addOrganisationsToCompetition(pathApplications)
    competitionToStartLists(comp)


}
