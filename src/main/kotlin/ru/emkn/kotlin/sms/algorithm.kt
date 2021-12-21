package ru.emkn.kotlin.sms

import graphics.compeTition
import mu.KotlinLogging
import java.io.File

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

fun startOnCl(eventFile: File, applications: List<File>, folder: String): List<List<String>> {
    val competition = makeCompetition(eventFile, folder, applications)
    saveCompetition(folder, competition)
    myDB.saveCompetition(competition)
    compeTition = competition
    return createStartProtocols(folder, competition)

}

fun finalResOnCl(classesFile: File, coursesFile: File, splitsFiles: List<File>, folder: String): List<List<String>> {
    Input.coursesFile = coursesFile
    Input.splitsFiles = splitsFiles
    Input.classesFile = classesFile
    Input.splitsMap = splitsInputNew(Input.splitsFiles).mapValues {
        it.value.list.map { MutableSplit(it.name, it.time) }.toMutableList()
    }
    val competition = compeTition
    generateResults(competition, folder)
    return generateTeamResults(competition, folder)
}


fun main(args: Array<String>) {

    if (args.isEmpty()) {
        println("pass parameters to command line arguments and try again")
    } else {
        when (args[0]) {
            "start" -> {
                checkArgsSize(args, 4)
                logger.info { "переходим к созданию соревнования, получая из файла event его название и дату" }
                val competition = makeCompetition(
                    File(args[1]),
                    args[3],
                    File(args[2]).listFiles().map { it!! }) //path to file event.csv

                createStartProtocols(args[3], competition)
                myDB.saveCompetition(competition)
                saveCompetition(args[3], competition) //saves start log in the path with pathName 'comp'
            }
            "finish" -> {
                checkArgsSize(args, 5)
                val competition = recreateSavedCompetition(File(args[4]))
                Input.classesFile = File(args[1]) //path to file with classes
                Input.coursesFile = File(args[2]) //path to file with courses
                Input.splitsFiles = File(args[3]).listFiles().map { it!! } //path to foldr with splits
                generateResults(competition, args[4])
                generateTeamResults(competition, args[4])

            }
            else -> {
                println("change first argument of command line to 'start' or 'finish' and try again!")
            }
        }
    }
}
