package ru.emkn.kotlin.sms

class Participant(
    val ageGroup: String?,
    val surname: String?,
    val name: String?,
    val birthYear: String?,
    val sportsCategory: String?,
) {
    var startNumber: String = ""

    override fun toString(): String {
        return "${this.surname} ${this.name}"
    }
}