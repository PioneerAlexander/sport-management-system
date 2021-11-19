package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

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
    for ((index, category) in mappedParticipants.keys.withIndex()) {
        csvWriter().open("testData/testStartProtocol$index.csv") {
            writeRow(
                listOf(category, "", "", "", "", "")
            )
            for ((number, participant) in mappedParticipants[category]!!.withIndex()) {
                writeRow(
                    listOf(
                        "$index$number", participant.surname, participant.name,
                        participant.birthYear, participant.sportsCategory, "time"
                    )
                )
            }
        }
    }
}

fun main(args: Array<String>) {
    val a = mutableListOf<Organisation>()
    for (i in 1..15) {
        a.add(applicationToOrg("sample-data/applications/application$i.csv"))
    }
    val comp = Competition("Test", "19.11.2021", a.toList())
    competitionToStartLists(comp)


}
