package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

class Competition {

    val sportClasses: List<SportClass>

    init {
        require(File("sample-data/classes.csv").isFile) {"No courses file"}
        val generator = mutableListOf<SportClass>()
        csvReader().open("sample-data/classes.csv"){
            readNext()
            readAllAsSequence().forEach {
                require(it.size == 2) {"Wrong classes file"}
                generator.add(SportClass(it[0], Distance(it[1])))
            }
        }
        sportClasses = generator
    }
}