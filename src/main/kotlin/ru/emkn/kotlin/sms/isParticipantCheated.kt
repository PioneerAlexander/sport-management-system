package ru.emkn.kotlin.sms

fun isNotCheated(participant: Participant): Boolean {
    if (participant.actualPath.size != participant.checkpoints.size) {  //lightCheck
        logger.info { "$participant дисквалифицирован из-за неверного количества контрольных пунктов" }
        return false
    }
    fun startTimeCheck(): Boolean {
        if (participant.actualPath.isEmpty()) {
            return false
        }
        return (participant.startTime < participant.actualPath[0].time)
    }

    if (!startTimeCheck()) {
        logger.info { "$participant дисквалифицирован из-за неверного времени" }
        return false
    }
    fun containerCheck(): Boolean {
        if (participant.actualPath.isEmpty()) {
            return false
        }
        return participant.checkpoints == participant.actualPath.map { it.name }
    }

    if (!containerCheck()) {
        logger.info { "$participant дисквалифицирован из-за неверного прохождения контрольных пунктов" }
        return false
    }
    return true
}



