package ru.emkn.kotlin.sms
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader


fun main(args: Array<String>) {
    //check how kotlin-csv works https://github.com/doyaaaaaken/kotlin-csv
    csvReader().open("sample-data/applications/application1.csv") {
        val org = Organisation(readNext()!![0])
        readAllWithHeaderAsSequence().forEach { row: Map<String, String>->
            org.addMember(Participant(row["Г.р"], row["Фамилия"],row["Имя"],row["Группа"],row["Разр."]))
        }
        println(org)
    }
}
