package ru.emkn.kotlin.sms
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlin.reflect.typeOf


fun main(args: Array<String>) {
    //check how kotlin-csv works https://github.com/doyaaaaaken/kotlin-csv
    csvReader().open("sample-data/applications/application1.csv") {
        val org = Organisation(readNext()!![0])
        readNext()
        readAllAsSequence().forEach { row: List<String> ->
            println( row[3])
            org.addMember(Participant(row[0], row[1], row[2], row[3], row[4]))
        }
        println(org)
    }
}
