package ru.emkn.kotlin.sms

class Participant(
    val ageGroup: String,
    val surname: String,
    val name: String,
    val birthYear: String,
    val sportsCategory: String,
) {

    companion object{
        val mapOfStringDistance = getSportClasses()
        val mapFromNumberToParticipant: Map<String, Participant> = TODO()

    }

    var startNumber: String = ""

    override fun toString(): String {
        return "${this.surname} ${this.name}"
    }
    val checkpoints
        get() = mapOfStringDistance[ageGroup]?.checkpoints

    fun isNotCheated(): Boolean{
        TODO()
    }






}