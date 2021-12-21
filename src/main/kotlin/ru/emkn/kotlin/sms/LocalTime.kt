package ru.emkn.kotlin.sms

import java.time.LocalTime

fun LocalTime.forPrint(): String { //не обрезать конец строки, если в секундах нули
    val buf = StringBuilder(18)
    val hourValue = hour
    val minuteValue = minute
    val secondValue = second
    buf.append(if (hourValue < 10) "0" else "")
        .append(hourValue)
        .append(if (minuteValue < 10) ":0" else ":")
        .append(minuteValue)
    buf.append(if (secondValue < 10) ":0" else ":")
        .append(secondValue)
    return buf.toString()
}