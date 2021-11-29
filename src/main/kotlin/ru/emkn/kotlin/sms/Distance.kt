package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.lang.IndexOutOfBoundsException
import java.time.LocalTime


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


fun MutableMap<String, MutableList<Split>>.sortSplits() {
    this.forEach { list ->
        list.value.sortBy { it.time }
    }
}

//tags: "ByParticipantNum" and "BySplitsName"
fun splitsInput(tag: String, directoryPath: String): Map<String, List<Split>> {
    val generator = mutableMapOf<String, MutableList<Split>>()
    val directory = File(directoryPath)
    try {
        check(directory.isDirectory)
        if (directory.listFiles() == null) {
            logger.error { "Протоколы похождения дистанции не найдены" }
            return generator
        }
        for (file in directory.listFiles()!!) {
            try {
                if (tag == "ByParticipantNum") {
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
    return generator
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


data class Split(val name: String, val time: LocalTime)


class Distance(val name: String) {
    companion object {
        var mapOfDistancesCheckpoints: Map<String, List<String>> = mapOf()
    }

    val checkpoints: List<String>
        get() {
            if (name in mapOfDistancesCheckpoints.keys) {
                require(mapOfDistancesCheckpoints[name] != null)
                return mapOfDistancesCheckpoints[name]!!
            }
            return listOf()
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Distance

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

