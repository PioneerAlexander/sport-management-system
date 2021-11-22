package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import kotlinx.datetime.*
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

fun getMapOfDistancesCheckpoints(filePath: String = "sample-data/courses.csv"): Map<String, List<Int>> {
    require(File(filePath).isFile) { "No courses file" }
    val generatorOfMap = mutableMapOf<String, List<Int>>()
    csvReader().open(filePath) {
        readNext()
        readAllAsSequence().forEach { list ->
            generatorOfMap[list[0]] = list.subList(1, list.lastIndex+1).filter { it != "" }.map { it.toInt() }

        }
    }
    return generatorOfMap
}

fun getMapFromNumberToSplits(filePath: String = "sample-data/splits.csv"): Map<String, List<Split>>{
    val generator = mutableMapOf<String, List<Split>>()
    csvReader().open(filePath) {
        readAllAsSequence().forEach { list ->
            require(list.isNotEmpty())
            val key = list[0]
            val smth = list.subList(1,list.lastIndex+1).filter { it != "" }
            require(smth.size % 2 == 0) {"actual size is ${smth.size}"}
            val listGenerator = mutableListOf<Split>()
            for(i in smth.indices step 2){
                val tempIterable = smth[i+1].split(":").map { it.toInt() }
                listGenerator.add(Split(smth[i], LocalTime.of(tempIterable[0], tempIterable[1], tempIterable[2])))
            }
            generator[key] = listGenerator

        }
    }
    return  generator
}

data class Split(val name: String, val time:LocalTime)



class Distance(val name: String) {
    companion object{
        val mapOfDistancesCheckpoints = getMapOfDistancesCheckpoints()
    }
    // TODO(названия немного отличающиеся от заданных работают некорректно)
    // TODO( "Ж16  студенты"(from courses.csv) != "Ж16 студенты"(from classes.csv))

    val checkpoints = mapOfDistancesCheckpoints[name]
}

