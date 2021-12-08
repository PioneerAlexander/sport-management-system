package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.time.LocalTime

fun createStartProtocols(targetPath: String = "comp", competition: Competition) {
    val file = File(targetPath, "startProtocols")
    file.mkdirs()
    val mappedParticipants = competition.participants.groupBy { it.ageGroup }
    val numberLength = competition.size.toString().length
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