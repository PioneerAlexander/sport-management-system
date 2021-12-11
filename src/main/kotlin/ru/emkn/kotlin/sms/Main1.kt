package ru.emkn.kotlin.sms

import mu.KotlinLogging
import ru.emkn.kotlin.sms.InputTag.*

val logger = KotlinLogging.logger { }

fun checkArgsSize(args: Array<String>, size: Int) {
    if (args.size < size) {
        logger.warn { "not enough parameters in command line arguments to create ${args[0]} protocols" }
    } else if (args.size > size) {
        logger.warn { "too much parameters in command line arguments to create ${args[0]} protocols" }
    }
}

fun checkMapElement(string: String?): String {
    if (string == null) {
        logger.warn { "Неполные аргументы были заменены на пустую строку" }
        return ""
    }
    return string.toString()
}


fun main(args: Array<String>) {

    if (args.isEmpty()) {
        println("pass parameters to command line arguments and try again")
    } else {
        when (args[0]) {
            "start" -> {
                checkArgsSize(args, 4)
                logger.info { "переходим к созданию соревнования, получая из файла event его название и дату" }
                val competition = makeCompetition(args[1], args[3], args[2]) //path to file event.csv

                createStartProtocols(args[3], competition)
                saveCompetition(args[3], competition) //saves start log in the path with pathName 'comp'
            }
            "finish" -> {
                checkArgsSize(args, 6)
                val competition = recreateSavedCompetition(args[5])
                Input.inputTag = when (args[1]) {
                    "BySplitsName" -> BySplitsName
                    else -> ByParticipantNum
                }
                Input.classesPath = args[2] //path to file with classes
                Input.coursesPath = args[3] //path to file with courses
                Input.splitsPath = args[4] //path to foldr with splits
                generateResults(competition, args[5])
                generateTeamResults(competition, args[5])

            }
            else -> {
                println("change first argument of command line to 'start' or 'finish' and try again!")
            }
        }
    }
}
