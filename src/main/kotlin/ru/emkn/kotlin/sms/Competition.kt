package ru.emkn.kotlin.sms

import kotlinx.datetime.*
import ru.emkn.kotlin.sms.Organisation.Companion.applicationToOrg
import java.io.File


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
}
