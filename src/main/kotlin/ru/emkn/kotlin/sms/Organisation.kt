package ru.emkn.kotlin.sms

class Organisation(var name: String, val members: MutableList<Participant> = mutableListOf()) {

    constructor() : this("")

    fun addMember(participant: Participant) {
        this.members.add(participant)
    }

    override fun toString(): String {
        return "${this.name} has ${this.members.size} members"
    }

}