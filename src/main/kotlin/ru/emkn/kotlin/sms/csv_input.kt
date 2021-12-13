package ru.emkn.kotlin.sms


import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.InputTag.*
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.time.LocalTime

enum class InputTag {
    ByParticipantNum, BySplitsName
}


class Input {
    companion object {
        var inputTag: InputTag = ByParticipantNum
        var classesPath: String = "" // Path to csv file
        var coursesPath: String = "" // Path to csv file
        var splitsPath: String = ""  // Path to folder with csv files

        fun check(): Boolean {
            return scvFileCheck(classesPath) and (scvFileCheck(coursesPath) and splitsPathCheck(splitsPath))
        }

        private fun scvFileCheck(path: String): Boolean {
            if (!File(path).isFile) {
                logger.error { "Неверный путь файла $path" }
                return false
            }
            if (File(path).extension != ".csv") {
                logger.error { "Неверный формат файла $path" }
                return false
            }
            return true
        }

        private fun splitsPathCheck(path: String): Boolean {
            if (!File(path).isDirectory) {
                logger.error { "Неверный путь к директории с промежуточными результатами" }
                return false
            }
            File(path).list()!!.toList().forEach {
                if (it == null) {
                    logger.warn { "Неверный файл промежуточного результата" }
                }
                if (!scvFileCheck("$path/$it")) {
                    return false
                }
            }
            return true
        }
    }
}

data class Distance(val name: String)


fun List<String>?.nullToEmpty(): List<String> {
    if (this == null) {
        logger.warn { "input error" }
        return listOf()
    }
    return this
}

fun NeededPath?.nullToEmpty(): NeededPath {
    if (this == null) {
        logger.warn { "input error" }
        return NeededPath(listOf())
    }
    return this
}


fun getMapGroupToNeededPathOld(classesPath: String, coursesPath: String): Map<String, NeededPath> {
    val mapOfDistancesCheckpoints = getMapOfDistancesCheckpoints(coursesPath)
    return getSportClasses(classesPath).mapValues { checkpoint ->
        NeededPath(mapOfDistancesCheckpoints[checkpoint.value.name].nullToEmpty()
            .map { PathSingleton(1, listOf(it)) })
    }
}

fun getMapGroupToNeededPathNew(classesPath: String, coursesPath: String): Map<String, NeededPath> {
    val mapOfDistancesCheckpoints = getMapDistanceNameToNeededPath(coursesPath)
    return getSportClasses(classesPath).mapValues { checkpoint ->
        mapOfDistancesCheckpoints[checkpoint.value.name].nullToEmpty()
    }
}

fun getSportClasses(filePath: String = "sample-data/classes.csv"): Map<String, Distance> {
    val generator = mutableMapOf<String, Distance>()
    try {
        check(File(filePath).isFile) { "No courses file" }
        csvReader().open(filePath) {
            readNext()
            readAllAsSequence().forEach {
                check(it.size == 2) { "Wrong classes file" }
                generator[it[0]] = Distance(it[1])
            }
        }
    } catch (ex: Exception) {
        logger.error { "Неверные данные в $filePath" }
    }
    return generator
}

// последняя отметка должна быть фенилом
fun getMapOfDistancesCheckpoints(filePath: String = "sample-data/courses.csv"): Map<String, List<String>> {
    check(File(filePath).isFile) { "No courses file" }
    val generatorOfMap = mutableMapOf<String, List<String>>()
    try {
        csvReader().open(filePath) {
            readNext()
            readAllAsSequence().forEach { list ->
                generatorOfMap[list[0]] = list.subList(1, list.lastIndex + 1).filter { it != "" }

            }
        }
    } catch (ex: Exception) {
        logger.error { "Неверные данные в $filePath" }
    }
    return generatorOfMap
}

/*
,NAME, - один чекпоит который надо посетить после предыдущего блока -> PathSingleton(1, [NAME])
,!NAME1 NAME2 NAME3, - нужно посетить один чекпоит из перечисленных на выбор -> PathSingleton(1, [NAME1, NAME2, NAME3])
,~NUMBER NAME1 NAME2 NAME3, - нужно посетить ровно NUMBER различных чекпоитов из перечисленных на выбор
-> PathSingleton(1, [NAME1, NAME2, NAME3])
 */

fun pathSingletonFromString(string: String): PathSingleton {
    return if (string[0] == '!') {
        PathSingleton(1, string.substring(1, string.lastIndex + 1).split(" "))
    } else if (string[0] == '~') {
        val withoutMarkerSign = string.substring(1, string.lastIndex + 1).split(" ")
        PathSingleton(withoutMarkerSign[0].toInt(), withoutMarkerSign.subList(1, withoutMarkerSign.lastIndex + 1))
    } else {
        PathSingleton(1, listOf(string))
    }
}

fun pathLangToNeededPath(list: List<String>): NeededPath {
    val neededPathGenerator = mutableListOf<PathSingleton>()
    list.forEach {
        neededPathGenerator.add(pathSingletonFromString(it))
    }
    return NeededPath(neededPathGenerator)
}

fun getMapDistanceNameToNeededPath(filePath: String = "sample-data/courses.csv"): Map<String, NeededPath> {
    val mapGenerator = mutableMapOf<String, NeededPath>()
    try {
        csvReader().open(filePath) {
            readNext()
            readAllAsSequence().forEach { list ->
                try {
                    mapGenerator[list[0]] =
                        pathLangToNeededPath(list.subList(1, list.lastIndex + 1).filter { it != "" })
                } catch (e: Exception) {
                    logger.error { "Неверный формат дистанции группы ${list[0]}" }
                }


            }
        }
    } catch (ex: Exception) {
        logger.error { "Неверные данные в $filePath ($ex)" }
    }
    return mapGenerator
}


fun MutableMap<String, MutableList<Split>>.sortSplits() {
    this.forEach { list ->
        list.value.sortBy { it.time }
    }
}

//tags: "ByParticipantNum" and "BySplitsName"
fun splitsInput(tag: InputTag, directoryPath: String): Map<String, ActualPath> {
    val generator = mutableMapOf<String, MutableList<Split>>()
    val directory = File(directoryPath)
    try {
        check(directory.isDirectory)
        if (directory.listFiles() == null) {
            logger.error { "Протоколы похождения дистанции не найдены" }
            return mapOf()
        }
        for (file in directory.listFiles()!!) {
            try {
                if (tag == ByParticipantNum) {
                    generator.addSplitsNameTimeFromFile(file)
                } else {
                    generator.addSplitsParticipantNameTimeFile(file)
                }
            } catch (e: IndexOutOfBoundsException) {
                logger.warn { "Неверные данные в файле ${file.name}" }
            }
        }
    } catch (e: IllegalStateException) {
        logger.error { "Неверная директория с пор межуточными отметками участников" }
    }
    generator.sortSplits()

    return generator.mapValues { ActualPath(it.value) }
}

fun splitsInputNew(directoryPath: String): Map<String, ActualPath> {
    val generator = mutableMapOf<String, MutableList<Split>>()
    val directory = File(directoryPath)
    try {
        check(directory.isDirectory)
        for (file in directory.listFiles()!!) {
            try {
                generator.addSplitsData(file)
            } catch (e: Exception) {
                logger.warn { "Неверные данные в файле ${file.name} ($e)" }
            }
        }
    } catch (e: IllegalStateException) {
        logger.error { "Неверная директория с пор межуточными отметками участников" }
    }
    generator.sortSplits()

    return generator.mapValues { ActualPath(it.value) }
}

// in try catch expression
fun MutableMap<String, MutableList<Split>>.addSplitsData(file: File) {
    csvReader().open(file) {
        val firstLine = readNext()
        if (firstLine != null) {
            if (firstLine[0] == "Participant") {
                this@addSplitsData.addSplitsNameTimeFromFileNew(file)
            } else if (firstLine[0] == "Checkpoint") {
                this@addSplitsData.addSplitsParticipantNameTimeFileNew(file)
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}

fun MutableMap<String, MutableList<Split>>.addSplitsNameTimeFromFileNew(file: File) {
    csvReader().open(file) {
        val numList = readNext()
        check(numList != null)
        readAllAsSequence().forEach { list ->
            val num = numList[1]
            val tempIterable = list[1].split(":").map { it.toInt() }
            val splitToAdd = Split(
                list[0],
                LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])
            )
            if (num in this@addSplitsNameTimeFromFileNew.keys) {
                if (!this@addSplitsNameTimeFromFileNew[num]!!.contains(splitToAdd)) {
                    this@addSplitsNameTimeFromFileNew[num]?.add(splitToAdd)
                }
            } else {
                this@addSplitsNameTimeFromFileNew[num] =
                    mutableListOf(Split(list[0], LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])))
            }
        }
    }
}

fun MutableMap<String, MutableList<Split>>.addSplitsParticipantNameTimeFileNew(file: File) {
    csvReader().open(file) {
        val nameList = readNext()
        check(nameList != null)
        readAllAsSequence().forEach { list ->
            val name = nameList[1]
            val tempIterable = list[1].split(":").map { it.toInt() }
            val splitToAdd = Split(
                name,
                LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])
            )
            if (list[0] in this@addSplitsParticipantNameTimeFileNew.keys) {
                if (!this@addSplitsParticipantNameTimeFileNew[list[0]]!!.contains(splitToAdd)) {
                    this@addSplitsParticipantNameTimeFileNew[list[0]]?.add(splitToAdd)
                }
            } else {
                this@addSplitsParticipantNameTimeFileNew[list[0]] =
                    mutableListOf(splitToAdd)
            }
        }

    }
}

fun MutableMap<String, MutableList<Split>>.addSplitsNameTimeFromFile(file: File) {
    csvReader().open(file) {
        val numList = readNext()
        if (numList != null) {
            readAllAsSequence().forEach { list ->
                val num = numList[0]
                val tempIterable = list[1].split(":").map { it.toInt() }
                if (num in this@addSplitsNameTimeFromFile.keys) {
                    this@addSplitsNameTimeFromFile[num]?.add(
                        Split(
                            list[0],
                            LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])
                        )
                    )
                } else {
                    this@addSplitsNameTimeFromFile[num] =
                        mutableListOf(Split(list[0], LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])))
                }
            }
        } else {
            logger.warn { "Неверный формат файла ${file.name}" }
        }
    }
}

fun MutableMap<String, MutableList<Split>>.addSplitsParticipantNameTimeFile(file: File) {
    csvReader().open(file) {
        val nameList = readNext()
        if (nameList != null) {
            readAllAsSequence().forEach { list ->
                val name = nameList[0]
                val tempIterable = list[1].split(":").map { it.toInt() }
                if (list[0] in this@addSplitsParticipantNameTimeFile.keys) {
                    this@addSplitsParticipantNameTimeFile[list[0]]?.add(
                        Split(
                            name,
                            LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])
                        )
                    )
                } else {
                    this@addSplitsParticipantNameTimeFile[list[0]] =
                        mutableListOf(Split(name, LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])))
                }
            }
        } else {
            logger.warn { "Неверный формат файла ${file.name}" }
        }
    }
}