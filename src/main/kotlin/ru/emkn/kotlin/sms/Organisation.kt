package ru.emkn.kotlin.sms

class Organisation(var name: String, val members: MutableList<Participant> = mutableListOf()) {

    constructor() : this("")

    fun addMember(participant: Participant) {
        this.members.add(participant)
    }

    override fun toString(): String {
        return this.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Organisation

        if (name != other.name) return false
        if (!members.containsAll(other.members)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + members.hashCode()
        return result
    }


}