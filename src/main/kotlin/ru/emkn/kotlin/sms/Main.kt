package ru.emkn.kotlin.sms
import mu.KotlinLogging
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.*

val logger = KotlinLogging.logger { }

fun getOrEmptyString(string: String?): String = string ?: ""

fun applicationToOrg(fileName: String): Organisation {
    //check how kotlin-csv works https://github.com/doyaaaaaken/kotlin-csv
    val org = Organisation()
    csvReader().open(fileName) {
        org.name = readNext()!![0]
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            org.addMember(
                Participant(
                    getOrEmptyString(row["Группа"]),
                    getOrEmptyString(row["Фамилия"]),
                    getOrEmptyString(row["Имя"]),
                    getOrEmptyString(row["Г.р."]),
                    getOrEmptyString(row["Разр."]),
                    org
                )
            )
        }
    }
    return org
}


fun makeCompetition(pathEvent: String): Competition {
    var eventName = ""
    var eventDate = LocalDate(2021, 11, 21)
    csvReader().open(pathEvent) {
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            require(row.size == 2)
            eventName = row["Название"]!!
            eventDate = LocalDate(
                row["Дата"]!!.split('.')[2].toInt(),
                row["Дата"]!!.split('.')[1].toInt(),
                row["Дата"]!!.split('.')[0].toInt()
            )
        }
    }
    return Competition(eventName, eventDate)
}

fun main(args: Array<String>) {
    val pathEvent = "sample-data/event.csv"
    val pathApplications = "sample-data/applications"
    val comp = makeCompetition(pathEvent)
    comp.addOrganisationsToCompetition(pathApplications)
    Competition.competitionToStartLists(comp)


}
