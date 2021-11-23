package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.*
import java.io.File
import java.time.LocalTime

class Competition(val name: String, val date: LocalDate, var orgs: List<Organisation> = listOf()) {

    val participants: List<Participant>
        get() = this.orgs.flatMap { it.members }

    val size: Int
        get() = this.participants.size

    fun addOrganisationsToCompetition(pathApplications: String) {
        val allOrgs = mutableListOf<Organisation>()
        for (file in File(pathApplications).listFiles()) {
            allOrgs.add(applicationToOrg(pathApplications + "/" + file.name))
        }
        this.orgs = allOrgs
    }

    val sportClasses = getSportClasses()

    companion object {
        fun competitionToStartLists(comp: Competition) {
            val mappedParticipants = comp.participants.groupBy { it.ageGroup }
            val numberLength = comp.size.toString().length
            var startNumber = 0
            var time = LocalTime.of(12, 0, 0, 0)
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
    }
}


fun LocalTime.forPrint(): String {
    val buf = StringBuilder(18)
    val hourValue = hour
    val minuteValue = minute
    val secondValue = second
    buf.append(if (hourValue < 10) "0" else "")
        .append(hourValue)
        .append(if (minuteValue < 10) ":0" else ":")
        .append(minuteValue);
    buf.append(if (secondValue < 10) ":0" else ":")
        .append(secondValue)
    return buf.toString();
}