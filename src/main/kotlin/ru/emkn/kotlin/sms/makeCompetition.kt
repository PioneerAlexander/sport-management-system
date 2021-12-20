package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import java.io.File

fun addOrganisations(applications: List<File>): List<Organisation> {

    return applications.map { Organisation.applicationToOrg(it) }
}

fun makeCompetition(eventFile: File, targetPath: String = "comp", applications: List<File>): Competition {
    val mainDirectory = File(System.getProperty("user.dir"), targetPath) //currentWorkingDirectory
    mainDirectory.mkdirs() //это место мы доделали, здесь создается директория
    var eventName = ""
    var eventDate = LocalDate.parse("2021-11-21")
    var date: List<String>
    csvReader().open(eventFile) {
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            check(row.size == 2) { "Нет названия или даты соревнования" }
            eventName = checkMapElement(row["Название"])
            date = checkMapElement(row["Дата"]).split('.')

            eventDate = LocalDate(
                date[2].toInt(),
                date[1].toInt(),
                date[0].toInt()
            )
        }
    }
    return Competition(eventName, eventDate, addOrganisations(applications))
}