package ru.emkn.kotlin.sms

import java.time.LocalTime


class Participant(
    val ageGroup: String,
    val surname: String,
    val name: String,
    val birthYear: String,
    val sportsCategory: String,
    val organisation: Organisation
) {

    companion object {
        val mapOfStringDistance = getSportClasses()

        //val mapFromNumberToParticipant: Map<String, Participant> = TODO()
        val mapFromNumberToSplits: Map<String, List<Split>> = getMapFromNumberToSplits()

    }

    var startNumber: String = ""
    var startTime: LocalTime = LocalTime.of(0, 0, 0, 0)
    var finishTime: LocalTime = LocalTime.of(0, 0, 0, 0)

    override fun toString(): String {
        return "${this.surname} ${this.name}"
    }

    val checkpoints: List<String>
        get() {
            if (ageGroup in mapOfStringDistance.keys) {
                return mapOfStringDistance[ageGroup]!!.checkpoints
            }
            return listOf()
        }

    val actualPath: List<Split>
        get() {
            if (startNumber in mapFromNumberToSplits.keys) {
                return mapFromNumberToSplits[startNumber]!!
            }
            return listOf()
        }

    fun isNotCheated(): Boolean {
       return lightCheck() and (timeCheck()) and (containerCheck())
    }

    fun lightCheck(): Boolean = (actualPath.size == checkpoints.size + 2)

    fun timeCheck(): Boolean {
        if (actualPath.isEmpty()) {
            return false
        }
        var ans = (startTime == actualPath[0].time)
        for (i in 1 until actualPath.size) {
            ans = ans and (actualPath[i - 1].time < actualPath[i].time)
        }
        return ans
    }

    fun containerCheck(): Boolean {
        if (actualPath.isEmpty()) {
            return false
        }
        val tempList = actualPath.map { it.name }
        return checkpoints == tempList.subList(1, tempList.lastIndex)
    }


}