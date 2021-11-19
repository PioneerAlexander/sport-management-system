package ru.emkn.kotlin.sms

class Organisation (val name: String, var members: MutableList<Participant> = mutableListOf()){

    fun addMember(participant: Participant){
        this.members.add(participant)
    }

    override fun toString(): String {
        return "${this.name} has ${this.members.size} members"
    }
}