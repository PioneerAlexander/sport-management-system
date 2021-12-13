package ru.emkn.kotlin.sms
import java.time.LocalTime

data class PathSingleton(val numberOfVisits: Int, val checkpointsOptions: List<String>)

data class NeededPath(val list: List<PathSingleton>)

data class Split(val name: String, val time: LocalTime)

data class ActualPath(val list: List<Split>) // отсортирован по времени

data class MapsForParticipant(
    val groupToNeededPath: Map<String, NeededPath>, // group -> distance -> map
    val numToActualPath: Map<String, ActualPath>
)

fun mapsGenerator(): MapsForParticipant { // <- INPUT
     return oldMapsGenerator() //TODO()
}

fun oldMapsGenerator(): MapsForParticipant { // <- INPUT
    return MapsForParticipant(
        getMapGroupToNeededPathNew(Input.classesPath, Input.coursesPath),
        splitsInput(Input.inputTag, Input.splitsPath)
    )
}




class ParticipantsPath(private val participant: Participant) {
    companion object {
        val maps = mapsGenerator()
    }

    private val neededPath: NeededPath
        get() {
            if (maps.groupToNeededPath[participant.ageGroup] == null) {
                logger.warn { "Не найдена информация о дистанции возрастной группы ${participant.ageGroup}" }
                return NeededPath(listOf()) // empty list
            }
            return maps.groupToNeededPath[participant.ageGroup]!! //null check is done upper
        }

    private val actualPath: ActualPath
        get() {
            if (maps.numToActualPath[participant.startNumber] == null) {
                logger.warn { "Не найдена информация о прохождении дистанции участником ${participant.startNumber}" }
                return ActualPath(listOf())
            }
            return maps.numToActualPath[participant.startNumber]!! // null check is done upper
        }

    val finishTime: LocalTime
        get() {
            if (maps.numToActualPath[participant.startNumber] == null) {
                logger.warn { "Не найдена информация о прохождении дистанции участником ${participant.startNumber}" }
                return LocalTime.of(0, 0, 0, 0)
            }
            return maps.numToActualPath[participant.startNumber]!!.list.last().time // null check is done upper
        }

    fun isNotCheated(): Boolean {
        if (!startTimeCheck()) {
            logger.info { "Участник номе ${participant.startNumber} дисквалифицирован" }
            return false
        }
        if (!containerCheck()) {
            logger.info { "Участник номе ${participant.startNumber} дисквалифицирован" }
            return false
        }
        return true
    }

    private fun startTimeCheck(): Boolean {
        if (actualPath.list.isEmpty()) {
            return false
        }
        return (participant.startTime < actualPath.list[0].time)
    }

    private fun containerCheck(): Boolean {
        if (actualPath.list.isEmpty()) {
            return false
        }
        return neededPath.doesSuits(actualPath)
    }


}

fun NeededPath.doesSuits(real: ActualPath): Boolean {
    var curState = true
    var itInReal = 0
    val namesOfRealList = real.list.map { it.name }
    this.list.forEach { pathSingleton ->
        val checkSet = namesOfRealList.subList(itInReal, itInReal + pathSingleton.numberOfVisits).toSet()
        curState = curState and (checkSet.size == pathSingleton.numberOfVisits)
        curState = curState and(pathSingleton.checkpointsOptions.containsAll(checkSet))
        itInReal += pathSingleton.numberOfVisits
    }
    return curState
}
