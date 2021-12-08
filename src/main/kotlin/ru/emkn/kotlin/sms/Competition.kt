package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.*
import java.io.File
import java.time.LocalTime

fun makeCompetition(pathEvent: String, targetPath: String = "comp"): Competition {
    val mainDirectory = File(System.getProperty("user.dir"), targetPath) //currentWorkingDirectory
    mainDirectory.mkdirs()
    var eventName = ""
    var eventDate = LocalDate.parse("2021-11-21")
    check(File(pathEvent).exists()) {
        logger.error { "Не существует файла с именем $pathEvent" }
        "Не существует файла с именем $pathEvent"
    }
    csvReader().open(pathEvent) {
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            check(row.size == 2) { "Нет названия или даты соревнования" }
            eventName = checkMapElement(row["Название"])
            eventDate = LocalDate(
                checkMapElement(row["Дата"]).split('.')[2].toInt(),
                checkMapElement(row["Дата"]).split('.')[1].toInt(),
                checkMapElement(row["Дата"]).split('.')[0].toInt()
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
    val organisations = mutableListOf<Organisation>()
    val mappedParticipants = allParticipants.groupBy { it.organisation }
    for (org in mappedParticipants.keys) {
        val currentOrganisation = Organisation(org, mappedParticipants[org] as MutableList<Participant>)
        organisations.add(currentOrganisation)
    }
    return Competition(eventName, eventDate, organisations.toList())
}

class Competition(val name: String, val date: LocalDate, var orgs: List<Organisation> = listOf()) {
    var inputTag: String = "ByParticipantNum"
    val participants: List<Participant>
        get() = this.orgs.flatMap { it.members }

    val size: Int
        get() = this.participants.size

    fun addOrganisationsToCompetition(pathApplications: String) {
        val allOrgs = mutableListOf<Organisation>()
        check(File(pathApplications).listFiles() != null) { "Нет организаций участников" }
        for (file in File(pathApplications).listFiles()!!) {
            allOrgs.add(applicationToOrg(pathApplications + "/" + file.name))
        }
        this.orgs = allOrgs
    }

    var classesPath: String = "" //когда в соревнование первый раз передается classesPath, меняется map в Participant
        set(value) {
            Participant.mapOfStringDistance = getSportClasses(value)
            field = value
        }

    var splitsPath: String = "" //когда в соревнование первый раз передается splitsPath, меняется map в Participant
        set(value) {
            Participant.mapFromNumberToSplits = splitsInput(inputTag, value)
            field = value
        }

    var coursesPath: String = "" //когда в соревнование первый раз передается splitsPath, меняется map в Distance
        set(value) {
            Distance.mapOfDistancesCheckpoints = getMapOfDistancesCheckpoints(value)
            field = value
        }


    fun createStartProtocols(targetPath: String = "comp") {
        val file = File(targetPath, "startProtocols")
        file.mkdirs()
        val mappedParticipants = this.participants.groupBy { it.ageGroup }
        val numberLength = this.size.toString().length
        var startNumber = 0
        var time = LocalTime.of(12, 0, 0, 0)
        for ((index, category) in mappedParticipants.keys.withIndex()) {
            csvWriter().open("comp/startProtocols/StartProtocol$index.csv") {
                writeRow(
                    listOf(category, "", "", "", "", "")
                )
                for (participant in mappedParticipants[category]!!.shuffled()) {
                    var participantNumber = startNumber.toString()
                    while (participantNumber.length < numberLength) {
                        participantNumber = "0$participantNumber"
                    }
                    participant.startNumber = participantNumber
                    participant.startTime = time
                    writeRow(
                        listOf(
                            participantNumber, participant.surname, participant.name,
                            participant.birthYear, participant.sportsCategory, time.forPrint()
                        )
                    )
                    time = time.plusMinutes(1)
                    startNumber += 1
                }
            }
        }
    }

    fun save(folderName: String = "comp") {
        csvWriter().open("$folderName/data.csv") {
            writeRow(this@Competition.name, this@Competition.date)
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
            for (participant in this@Competition.participants) {
                writeRow(participant.toList())
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Competition

        if (name != other.name) return false

        if (date != other.date) return false

        if (!orgs.containsAll(other.orgs) || !other.orgs.containsAll(orgs)) return false

        if (!participants.containsAll(other.participants) || !other.participants.containsAll(participants)) return false

        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + orgs.hashCode()
        result = 31 * result + participants.hashCode()
        result = 31 * result + size
        return result
    }

    companion object {


        fun applicationToOrg(fileName: String): Organisation {
            //check how kotlin-csv works https://github.com/doyaaaaaken/kotlin-csv
            var name = ""
            val members: MutableList<Participant> = mutableListOf()
            csvReader().open(fileName) {
                val firstStringList = readNext()
                try {
                    check(firstStringList != null)
                    check(firstStringList.isNotEmpty())
                    name = firstStringList[0]
                    readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                        members.add(
                            Participant(
                                checkMapElement(row["Группа"]),
                                checkMapElement(row["Фамилия"]),
                                checkMapElement(row["Имя"]),
                                checkMapElement(row["Г.р."]),
                                checkMapElement(row["Разр."]),
                                name
                            )
                        )
                    }
                } catch (e: Exception) {
                    "Некорректный протокол организации"
                }
            }
            return Organisation(name, members)
        }
    }

}


fun LocalTime.forPrint(): String { //не обрезать конец строки, если в секундах нули
    val buf = StringBuilder(18)
    val hourValue = hour
    val minuteValue = minute
    val secondValue = second
    buf.append(if (hourValue < 10) "0" else "")
        .append(hourValue)
        .append(if (minuteValue < 10) ":0" else ":")
        .append(minuteValue)
    buf.append(if (secondValue < 10) ":0" else ":")
        .append(secondValue)
    return buf.toString()
}