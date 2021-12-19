package ru.emkn.kotlin.sms

import java.time.LocalTime

class Participant(
    val ageGroup: String,
    val surname: String,
    val name: String,
    val birthYear: String,
    val sportsCategory: String,
    val organisation: String
) {


    var startNumber: String = ""
    var startTime: LocalTime = LocalTime.of(0, 0, 0, 0)
    var points: Int = 0


    override fun toString(): String {
        return "${this.surname} ${this.name}"
    }

    fun toList() = listOf<String>(
        this.surname,
        this.name,
        this.birthYear,
        this.ageGroup,
        this.sportsCategory,
        this.organisation,
        this.startNumber,
        this.startTime.hour.toString(),
        this.startTime.minute.toString(),
        this.startTime.second.toString(),
    )

    val finishTime
        get() = ParticipantsPath(this).finishTime

    val isNotCheated: Boolean
    get() = ParticipantsPath(this).isNotCheated()



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Participant

        if (ageGroup != other.ageGroup) return false
        if (surname != other.surname) return false
        if (name != other.name) return false
        if (birthYear != other.birthYear) return false
        if (sportsCategory != other.sportsCategory) return false
        if (organisation != other.organisation) return false
        if (startNumber != other.startNumber) return false
        if (startTime != other.startTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ageGroup.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + birthYear.hashCode()
        result = 31 * result + sportsCategory.hashCode()
        result = 31 * result + organisation.hashCode()
        result = 31 * result + startNumber.hashCode()
        result = 31 * result + startTime.hashCode()
        return result
    }


}