package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.lang.IndexOutOfBoundsException
import java.security.AlgorithmParameterGenerator
import java.time.LocalTime


fun getSportClasses(filePath: String = "sample-data/classes.csv"): Map<String, Distance> {
    require(File(filePath).isFile) { "No courses file" }
    val generator = mutableMapOf<String, Distance>()
    csvReader().open(filePath) {
        readNext()
        readAllAsSequence().forEach {
            require(it.size == 2) { "Wrong classes file" }
            generator[it[0]] = Distance(it[1])
        }
    }
    return generator
}

fun getMapOfDistancesCheckpoints(filePath: String = "sample-data/courses.csv"): Map<String, List<String>> {
    require(File(filePath).isFile) { "No courses file" }
    val generatorOfMap = mutableMapOf<String, List<String>>()
    try {
        csvReader().open(filePath) {
            readNext()
            readAllAsSequence().forEach { list ->
                generatorOfMap[list[0]] = list.subList(1, list.lastIndex + 1).filter { it != "" }

            }
        }
    } finally {
    }
    return generatorOfMap
}

//
fun getMapFromNumberToSplits(filePath: String = "sample-data/splits.csv"): Map<String, List<Split>> {
    val generator = mutableMapOf<String, List<Split>>()
    csvReader().open(filePath) {
        readAllAsSequence().forEach { list ->
            try {
                require(list.isNotEmpty())
                val key = list[0]
                val smth = list.subList(1, list.lastIndex + 1).filter { it != "" }
                require(smth.size % 2 == 0) { "actual size is ${smth.size}" }
                val listGenerator = mutableListOf<Split>()
                for (i in smth.indices step 2) {
                    val tempIterable = smth[i + 1].split(":").map { it.toInt() }
                    listGenerator.add(Split(smth[i], LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])))
                }
                generator[key] = listGenerator
            } finally {
            }
        }
    }
    return generator
}

fun MutableMap<String, MutableList<Split>>.sortSplits() {
    this.forEach { list ->
        list.value.sortBy { it.time }
    }
}

//tags: "ByParticipantNum" and "BySplitsName"
fun splitsInputByParticipantNum(tag: String, directoryPath: String): Map<String, List<Split>> {
    val generator = mutableMapOf<String, MutableList<Split>>()
    val directory = File(directoryPath)
    try {
        check(directory.isDirectory)
        if (directory.listFiles() == null){
            logger.error { "Протоколы похождения дистанции не найдены" }
            return generator
        }
        for (file in directory.listFiles()) {
            try {
                if (tag == "ByParticipantNum") {
                    generator.addSplitsNameTimeFromFile(file)
                } else {
                    generator.addSplitsParticipantNameTimeFile(file)
                }
            } catch (e: IndexOutOfBoundsException) {
                logger.error { "Неверные данные в файле ${file.name}" }
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
        val numL = readNext()
        if (numL != null) {
            readAllAsSequence().forEach {
                val num = numL[0]
                val tempIterable = it[1].split(":").map { it.toInt() }
                if (num in this@addSplitsNameTimeFromFile.keys) {
                    this@addSplitsNameTimeFromFile[num]?.add(
                        Split(
                            it[0],
                            LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])
                        )
                    )
                } else {
                    this@addSplitsNameTimeFromFile[num] =
                        mutableListOf(Split(it[0], LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])))
                }
            }
        } else {
            logger.warn { "Неверный формат файла ${file.name}" }
        }
    }
}

fun MutableMap<String, MutableList<Split>>.addSplitsParticipantNameTimeFile(file: File) {
    csvReader().open(file) {
        val nameL = readNext()
        if (nameL != null) {
            readAllAsSequence().forEach {
                val name = nameL[0]
                val tempIterable = it[1].split(":").map { it.toInt() }
                if (it[0] in this@addSplitsParticipantNameTimeFile.keys) {
                    this@addSplitsParticipantNameTimeFile[it[0]]?.add(
                        Split(
                            name,
                            LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])
                        )
                    )
                } else {
                    this@addSplitsParticipantNameTimeFile[it[0]] =
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


    // TODO(названия немного отличающиеся от заданных работают некорректно)
    // TODO( "Ж16  студенты"(from courses.csv) != "Ж16 студенты"(from classes.csv))

    val checkpoints: List<String>
        get() {
            if (name in mapOfDistancesCheckpoints.keys) {
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

