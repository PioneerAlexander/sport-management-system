package ru.emkn.kotlin.sms

fun Participant.isNotCheated(): Boolean {
    if (this.actualPath.size != this.checkpoints.size) {  //lightCheck
        logger.info { "$this дисквалифицирован из-за неверного количества контрольных пунктов" }
        return false
    }
    fun startTimeCheck(): Boolean {
        if (this.actualPath.isEmpty()) {
            return false
        }
        return (this.startTime < this.actualPath[0].time)
    }

    if (!startTimeCheck()) {
        logger.info { "$this дисквалифицирован из-за неверного времени" }
        return false
    }
    fun containerCheck(): Boolean {
        if (this.actualPath.isEmpty()) {
            return false
        }
        return this.checkpoints == this.actualPath.map { it.name }
    }

    if (!containerCheck()) {
        logger.info { "$this дисквалифицирован из-за неверного прохождения контрольных пунктов" }
        return false
    }
    return true
}



