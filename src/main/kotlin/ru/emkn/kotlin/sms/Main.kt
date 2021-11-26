package ru.emkn.kotlin.sms

import mu.KotlinLogging
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.*
import java.io.File
import java.time.LocalTime

val logger = KotlinLogging.logger { }

fun makeCompetition(pathEvent: String, targetPath: String = "comp"): Competition {
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

fun recreateSavedCompetition(folderPath: String = "comp"): Competition {
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

    if (args.isEmpty()) {
        println("pass parameters to command line arguments and try again")
    } else {
        when (args[0]) {
            "start" -> {
                if (args.size < 3) {
                    logger.warn { "not enough parameters in command line arguments to create start protocols" }
                } else {
                    val competition = makeCompetition(args[1]) //path to file event.csv

                    competition.addOrganisationsToCompetition(args[2]) //way to directory with applications
                    competition.createStartProtocols()
                    competition.save() //saves start log in the path with pathName 'comp'
                }
            }
            "finish" -> {
                if (args.size < 4) {
                    println("not enough parameters in command line arguments to create finish protocols")
                } else {
                    val competition = recreateSavedCompetition()

                    competition.classesPath = args[1] //way to path with classes
                    competition.coursesPath = args[2] //way to path with courses
                    competition.splitsPath = args[3] //way to path with splits
                    generateResults(competition)
                }
            }
            else -> {
                println("change first argument of command line to 'start' or 'finish' and try again!")
            }
        }
    }
}
