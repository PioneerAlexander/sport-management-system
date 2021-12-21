package myDB

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import ru.emkn.kotlin.sms.Competition
import ru.emkn.kotlin.sms.Organisation
import ru.emkn.kotlin.sms.Participant
import ru.emkn.kotlin.sms.logger
import java.time.LocalTime

object CompetitionsT : IntIdTable("CompetitionsT") {
    //val sequelId: Column<Int> = integer("sequelId").uniqueIndex()
    val name: Column<String> = varchar("name", 255)
    val date: Column<String> = varchar("date", 255)

    fun toCompetition(row: ResultRow): Competition = Competition(
        row[name],
        LocalDate.parse(row[date]),
        ParticipantsT.select { ParticipantsT.competitionId eq row[id].value }.map { ParticipantsT.toParticipant(it) }
            .groupBy { it.organisation }
            .map { Organisation(it.key, it.value) })

    fun saveCompetition(competition: Competition) {
        val curCompId = CompetitionsT.insertAndGetId {
            it[name] = competition.name
            it[date] = competition.date.toString()
        }
        competition.participants.forEach { participant ->
            ParticipantsT.insertAndGetId {
                it[competitionId] = curCompId.value
                it[name] = participant.name
                it[surname] = participant.surname
                it[ageGroup] = participant.ageGroup
                it[birthYear] = participant.birthYear
                it[sportsCategory] = participant.sportsCategory
                it[organisation] = participant.organisation
                it[startNumber] = participant.startNumber
                it[stHour] = participant.startTime.hour
                it[stMinute] = participant.startTime.minute
                it[stSecond] = participant.startTime.second
            }
        }
    }
}


object ParticipantsT : IntIdTable("ParticipantsT") {
    //val sequelId: Column<Int> = integer("sequelId").uniqueIndex()
    val competitionId = integer("competitionId")
    val name: Column<String> = varchar("name", 255)
    val surname: Column<String> = varchar("surname", 255)
    val ageGroup: Column<String> = varchar("ageGroup", 255)
    val birthYear: Column<String> = varchar("birthYear", 255)
    val sportsCategory: Column<String> = varchar("sportsCategory", 255)
    val organisation: Column<String> = varchar("organisation", 255)
    val startNumber: Column<String> = varchar("startNumber", 255)
    val stHour: Column<Int> = integer("hour")
    val stMinute: Column<Int> = integer("minute")
    val stSecond: Column<Int> = integer("second")

    fun toParticipant(row: ResultRow): Participant {
        val participant = Participant(
            row[ageGroup],
            row[surname],
            row[name],
            row[birthYear],
            row[sportsCategory],
            row[organisation],
        )
        logger.info { row[startNumber] }
        participant.startNumber = row[startNumber]
        participant.startTime = LocalTime.of(row[stHour], row[stMinute], row[stSecond])
        return participant
    }
}


