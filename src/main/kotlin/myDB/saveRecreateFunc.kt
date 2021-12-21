package myDB

import ru.emkn.kotlin.sms.Competition
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


fun saveCompetition(competition: Competition) {
    Database.connect("jdbc:h2:./myh2file", "org.h2.Driver")
    transaction {
        SchemaUtils.create(CompetitionsT)
        SchemaUtils.create(ParticipantsT)
        CompetitionsT.deleteAll()
        ParticipantsT.deleteAll()
        CompetitionsT.saveCompetition(competition)
    }
}

fun recreateCompetition(): Competition {
    Database.connect("jdbc:h2:./myh2file", "org.h2.Driver")
    val competition = transaction {
        CompetitionsT.toCompetition(CompetitionsT.selectAll().first())
    }
    return competition
}

fun clean() {
    Database.connect("jdbc:h2:./myh2file", "org.h2.Driver")
    transaction {
        CompetitionsT.deleteAll()
        ParticipantsT.deleteAll()
    }
}