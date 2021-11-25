package ru.emkn.kotlin.sms

import mu.KotlinLogging
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.*
import java.io.File
import java.time.LocalTime

val logger = KotlinLogging.logger { }

fun makeCompetition(pathEvent: String, targetPath:String = "comp"): Competition {
    val mainDirectory = File(System.getProperty("user.dir"), targetPath)
    mainDirectory.mkdirs()
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
