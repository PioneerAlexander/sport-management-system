package ru.emkn.kotlin.sms

import mu.KotlinLogging
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.*
import java.io.File
import java.time.LocalTime

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
                    org.name
                )
            )
        }
    }
    return org
}


fun makeCompetition(pathEvent: String, targetPath:String = "comp"): Competition {
    val f = File(System.getProperty("user.dir"), targetPath)
    f.mkdirs()
    var eventName = ""
    var eventDate = LocalDate.parse("2021-11-21")
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

fun recreateSavedCompetition(folderPath: String): Competition {
    var eventName = ""
    var eventDate = LocalDate.parse("2021-11-21")
    val allParticipants = mutableListOf<Participant>()
    csvReader().open("$folderPath/data.csv") {
        val listedNameDate = readNext()
        eventName = listedNameDate!![0]
        eventDate = LocalDate.parse(listedNameDate!![1])
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            val currentParticipant = Participant(
                row["Группа"]!!,
                row["Фамилия"]!!,
                row["Имя"]!!,
                row["Г.р."]!!,
                row["Разряд"]!!,
                row["Команда"]!!.trim()
            )
            currentParticipant.startNumber = row["Номер"]!!
            currentParticipant.startTime =
                LocalTime.of(row["Час"]!!.toInt(), row["Минута"]!!.toInt(), row["Секунда"]!!.toInt())
            allParticipants.add(currentParticipant)
        }
    }
    val organisations = mutableListOf<Organisation>()
    val mappedParticipants = allParticipants.groupBy { it.organisation }
    for (org in mappedParticipants.keys) {
        val currentOrganisation = Organisation(org, mappedParticipants[org] as MutableList<Participant>)
        organisations.add(currentOrganisation)
    }
    return Competition(eventName, eventDate, organisations.toList())
}

fun main(args: Array<String>) {


}
