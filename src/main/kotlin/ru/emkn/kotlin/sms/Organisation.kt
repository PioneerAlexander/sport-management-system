package ru.emkn.kotlin.sms

class Organisation(val name: String, val members: List<Participant> = listOf()) {

    constructor() : this("")

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