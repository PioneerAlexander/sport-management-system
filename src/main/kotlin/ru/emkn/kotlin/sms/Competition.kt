package ru.emkn.kotlin.sms

import kotlinx.datetime.*


class Competition(val name: String, val date: LocalDate, val orgs: List<Organisation> = listOf()) {

    val participants: List<Participant>
        get() = this.orgs.flatMap { it.members }

    val size: Int
        get() = this.participants.size



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
