package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.datetime.*
import java.io.File

class Competition (val name: String, val date: String, var orgs: List<Organisation> = listOf()){

    val participants: List<Participant>
        get() = this.orgs.flatMap { it.members }

    val size: Int
        get() = this.participants.size

    fun addOrganisationsToCompetition( pathApplications: String){
        val allOrgs = mutableListOf<Organisation>()
        for (file in File(pathApplications).listFiles()) {
            allOrgs.add(applicationToOrg(pathApplications+ "/"+file.name))
        }
        this.orgs = allOrgs
    }
    
    val sportClasses = getSportClasses()

    companion object {
        fun competitionToStartLists(comp: Competition) {
            val mappedParticipants = comp.participants.groupBy { it.ageGroup }
            val numberLength = comp.size.toString().length
            var startNumber = 0
            var time = LocalDateTime(
                2021, 11, 21,
                12, 0, 0, 0
            ).toInstant(TimeZone.UTC)
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
                        writeRow(
                            listOf(
                                participantNumber, participant.surname, participant.name,
                                participant.birthYear, participant.sportsCategory, time.toString().substring(11..18)
                            )
                        )
                        time = time.plus(DateTimePeriod(minutes = 1), TimeZone.UTC)
                        startNumber += 1
                    }
                }
            }
        }
    }
}