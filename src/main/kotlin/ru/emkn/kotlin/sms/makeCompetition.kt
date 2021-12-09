package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.datetime.LocalDate
import java.io.File

fun addOrganisations(pathApplications: String): List<Organisation> {
    check(File(pathApplications).listFiles() != null) { "Нет организаций участников" }

    return File(pathApplications).listFiles()!!.map { Organisation.applicationToOrg(pathApplications + "/" + it.name) }
}

fun makeCompetition(pathEvent: String, targetPath: String = "comp", pathApplications: String): Competition {
    val mainDirectory = File(System.getProperty("user.dir"), targetPath) //currentWorkingDirectory
    mainDirectory.mkdirs() //это место мы доделали, здесь создается директория
    var eventName = ""
    var eventDate = LocalDate.parse("2021-11-21")
    var date: List<String>
    check(File(pathEvent).exists()) {
        logger.error { "Не существует файла с именем $pathEvent" }
        "Не существует файла с именем $pathEvent"
    }
    csvReader().open(pathEvent) {
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
    return Competition(eventName, eventDate, addOrganisations(pathApplications))
}