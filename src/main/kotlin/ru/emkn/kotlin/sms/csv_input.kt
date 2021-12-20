package ru.emkn.kotlin.sms


import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.time.LocalTime


class Input {
    companion object {
        var classesFile: File = File("") // Path to csv file
        var coursesFile: File = File("")
        var splitsFiles: List<File> = listOf()
        /*
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
            // File(path) существует
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

         */
    }
}

data class Distance(val name: String)

fun NeededPath?.nullToEmpty(): NeededPath {
    if (this == null) {
        logger.warn { "input error" }
        return NeededPath(listOf())
    }
    return this
}


fun getMapGroupToNeededPathNew(classesFile: File, coursesFile: File): Map<String, NeededPath> {
    val mapOfDistancesCheckpoints = getMapDistanceNameToNeededPath(coursesFile)
    return getSportClasses(classesFile).mapValues { checkpoint ->
        mapOfDistancesCheckpoints[checkpoint.value.name].nullToEmpty()
    }
}

fun getSportClasses(file: File): Map<String, Distance> {
    val generator = mutableMapOf<String, Distance>()
    try {
        check(file.isFile) { "No courses file" }
        csvReader().open(file) {
            readNext()
            readAllAsSequence().forEach {
                check(it.size == 2) { "Wrong classes file" }
                generator[it[0]] = Distance(it[1])
            }
        }
    } catch (ex: Exception) {
        logger.error { "Неверные данные в ${file.name}" }
    }
    return generator
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

fun getMapDistanceNameToNeededPath(file: File): Map<String, NeededPath> {
    val mapGenerator = mutableMapOf<String, NeededPath>()
    try {
        csvReader().open(file) {
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
        logger.error { "Неверные данные в ${file.name} ($ex)" }
    }
    return mapGenerator
}


fun MutableMap<String, MutableList<Split>>.sortSplits() {
    this.forEach { list ->
        list.value.sortBy { it.time }
    }
}


fun splitsInputNew(files: List<File>): Map<String, ActualPath> {
    val generator = mutableMapOf<String, MutableList<Split>>()
    try {
        for (file in files) {
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
