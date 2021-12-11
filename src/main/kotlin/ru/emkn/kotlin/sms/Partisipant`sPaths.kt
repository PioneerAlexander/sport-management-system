package ru.emkn.kotlin.sms

import java.time.LocalTime

data class PathSingletons(val numberOfVisits: Int, val checkpointsOptions: List<String>)

data class NeededPath(val list: List<PathSingletons>)

data class Split(val name: String, val time: LocalTime)

data class ActualPath(val list: List<Split>) // отсортирован по времени

data class MapsForParticipant(
    val groupToNeededPath: Map<String, NeededPath>, // group -> distance -> map
    val numToActualPath: Map<String, ActualPath>
)

fun mapsGenerator(): MapsForParticipant { // <- INPUT
    TODO()
}

fun List<PathSingletons>.doesSuitsPath(real: List<String>): Boolean{
    var curState = true
    var itInReal = 0
    this.forEach {
        for(i in 1..it.numberOfVisits){
            curState = curState and (real[itInReal] in it.checkpointsOptions)
            itInReal += 1
        }
    }
    return curState
}

class ParticipantsPath(private val participant: Participant) {
    companion object {
        val maps = mapsGenerator()
    }

    val checkpoints: NeededPath
        get() {
            if (maps.groupToNeededPath[participant.ageGroup] == null) {
                logger.warn { "Не найдена информация о дистанции возрастной группы ${participant.ageGroup}" }
                return NeededPath(listOf()) // empty list
            }
            return maps.groupToNeededPath[participant.ageGroup]!! //null check is done upper
        }

    val actualPath: ActualPath
        get() {
            if (maps.numToActualPath[participant.startNumber] == null) {
                logger.warn { "Не найдена информация о прохождении дистанции участником ${participant.startNumber}" }
                return ActualPath(listOf())
            }
            return maps.numToActualPath[participant.startNumber]!! // null check is done upper
        }

    val finishTime: LocalTime
    get() {
        if (maps.numToActualPath[participant.startNumber] == null){
            logger.warn { "Не найдена информация о прохождении дистанции участником ${participant.startNumber}" }
            return LocalTime.of(0, 0, 0, 0)
        }
        return maps.numToActualPath[participant.startNumber]!!.list.last().time // null check is done upper
    }
}

