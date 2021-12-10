package ru.emkn.kotlin.sms

import java.time.LocalTime

data class PathSingletons(val numberOfVisits: Int, val checkpointsOptions: List<String>)

data class NeededPath(val list: List<PathSingletons>)

data class Split(val name: String, val time: LocalTime)

data class ActualPath(val list: List<Split>)

data class MapsForParticipant(
    val groupToNeededPath: Map<String, NeededPath>,
    val numToActualPath: Map<String, ActualPath>
)

fun mapsGenerator(): MapsForParticipant {
    TODO()
}

class ParticipantsPaths(private val participant: Participant) {
    companion object {
        val maps = mapsGenerator()
    }


    val ageGroup: String
        get() = participant.ageGroup
    val surname: String
        get() = participant.surname
    val name: String
        get() = participant.name
    val birthYear: String
        get() = participant.birthYear
    val sportsCategory: String
        get() = participant.sportsCategory
    val organisation: String
        get() = participant.organisation
    val startNumber: String
        get() = participant.startNumber
    val startTime: LocalTime
        get() = participant.startTime

    val finishTime: LocalTime

    init {
        if (maps.numToActualPath[startNumber] == null){
            logger.warn { "Не найдена информация о прохождении дистанции участником $startNumber" }
        }
        finishTime = maps.numToActualPath[startNumber]!!.list.last().time // null check is done upper
    }

    val checkpoints: NeededPath
        get() {
            if (maps.groupToNeededPath[ageGroup] == null) {
                logger.warn { "Не найдена информация о дистанции возрастной группы $ageGroup" }
                return NeededPath(listOf()) // empty list
            }
            return maps.groupToNeededPath[ageGroup]!! //null check is done upper
        }
    val actualPath: ActualPath
        get() {
            if (maps.numToActualPath[startNumber] == null) {
                logger.warn { "Не найдена информация о прохождении дистанции участником $startNumber" }
                return ActualPath(listOf())
            }
            return maps.numToActualPath[startNumber]!! // null check is done upper
        }

    fun isNotCited(): Boolean {
        if (!startTimeCheck()) {
            logger.info { "Участник номе $startNumber дисквалифицирован" }
            return false
        }
        if (!containerCheck()) {
            logger.info { "Участник номе $startNumber дисквалифицирован" }
            return false
        }
        return true
    }

    private fun startTimeCheck(): Boolean {
        if (this.actualPath.list.isEmpty()) {
            return false
        }
        return (startTime < this.actualPath.list[0].time)
    }

    private fun containerCheck(): Boolean {
        if (this.actualPath.list.isEmpty()) {
            return false
        }
        return this.checkpoints.list == this.actualPath.list.map { it.name }
    }
}
