package ru.emkn.kotlin.sms

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class Organisation(val name: String, val members: List<Participant> = listOf()) {

    override fun toString(): String {
        return this.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Organisation

        if (name != other.name) return false
        if (!members.containsAll(other.members)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + members.hashCode()
        return result
    }

    companion object {
        fun applicationToOrg(fileName: String): Organisation {
            //check how kotlin-csv works https://github.com/doyaaaaaken/kotlin-csv
            var name = ""
            val members: MutableList<Participant> = mutableListOf()
            csvReader().open(fileName) {
                val firstStringList = readNext()
                try {
                    check(firstStringList != null)
                    check(firstStringList.isNotEmpty())
                    name = firstStringList[0]
                    readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                        members.add(
                            Participant(
                                checkMapElement(row["Группа"]),
                                checkMapElement(row["Фамилия"]),
                                checkMapElement(row["Имя"]),
                                checkMapElement(row["Г.р."]),
                                checkMapElement(row["Разр."]),
                                name
                            )
                        )
                    }
                } catch (e: Exception) {
                    "Некорректный протокол организации"
                }
            }
            return Organisation(name, members)
        }
    }
}