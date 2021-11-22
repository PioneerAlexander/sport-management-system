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
            try {
                require("Группа" in row.keys)
                require("Фамилия" in row.keys)
                require("Имя" in row.keys)
                require("Г.р." in row.keys)
                require("Разр." in row.keys)
            }
            catch (e:IllegalArgumentException) {

            }
            TODO("Skip next line")
            org.addMember(Participant(row["Группа"]!!, row["Фамилия"]!!, row["Имя"]!!, row["Г.р."]!!, row["Разр."]!!))
        }
    }
    return org
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

fun checkDistanceCompletion(participant: Participant): Boolean {
    TODO("сравнивать пройденные участником (participant.checkpoints) и те, которые он должен пройти," +
            "проверить галичие последнего и последовательность")
}

fun main(args: Array<String>) {
    val pathEvent = "sample-data/event.csv"
    val pathApplications = "sample-data/applications"
    val comp = makeCompetition(pathEvent)
    comp.addOrganisationsToCompetition(pathApplications)
    Competition.competitionToStartLists(comp)


}
