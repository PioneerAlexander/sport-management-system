package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

fun getSportClasses(): Map<String, Distance> {
    require(File("sample-data/classes.csv").isFile) { "No courses file" }
    val generator = mutableMapOf<String, Distance>()
    csvReader().open("sample-data/classes.csv") {
        readNext()
        readAllAsSequence().forEach {
            require(it.size == 2) { "Wrong classes file" }
            generator[it[0]] = Distance(it[1])
        }
    }
    return generator
}

fun getMapOfDistancesCheckpoints(): Map<String, List<Int>> {
    require(File("sample-data/courses.csv").isFile) { "No courses file" }
    val generatorOfMap = mutableMapOf<String, List<Int>>()
    csvReader().open("sample-data/courses.csv") {
        readNext()
        readAllAsSequence().forEach { list ->
            generatorOfMap[list[0]] = list.subList(1, list.lastIndex).filter { it != "" }.map { it.toInt() }

        }
    }
    return generatorOfMap
}



class Distance(val name: String) {
    companion object{
        val mapOfDistancesCheckpoints = getMapOfDistancesCheckpoints()
    }
    // TODO(названия немного отличающиеся от заданных работают некорректно)
    // TODO( "Ж16  студенты"(from courses.csv) != "Ж16 студенты"(from classes.csv))

    val checkpoints = mapOfDistancesCheckpoints[name]
}

