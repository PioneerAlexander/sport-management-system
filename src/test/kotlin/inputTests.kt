import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.InputTag.ByParticipantNum
import ru.emkn.kotlin.sms.InputTag.BySplitsName
import java.time.LocalTime
import kotlin.test.*

internal class TestOfInput {


    @Test
    fun testGetSportClasses() {
        val actual = getSportClasses("src/test/resources/test1_classes.csv")
        val expected = mapOf(
            Pair("1", Distance("МЖ9 10")),
            Pair("2", Distance("МЖ9 10"))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testGetMapOfDistancesCheckpoints() {
        val actual = getMapOfDistancesCheckpoints("src/test/resources/test1_courses.csv")
        val expected = mapOf(
            Pair("1", listOf("11", "12", "13", "14", "15", "16")),
            Pair("2", listOf("21", "22", "23", "24", "25", "26", "27", "28", "29")),
            Pair("3", listOf())
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testOfSplitsInputByParticipantName() {
        val actual = splitsInput(ByParticipantNum, "src/test/resources/splitsByNum")
        val expected = mutableMapOf<String, List<Split>>()
        expected["007"] = listOf(
            Split("1km", LocalTime.of(12, 7, 15)),
            Split("2km", LocalTime.of(12, 11, 36)),
            Split("3km", LocalTime.of(12, 13, 29)),
            Split("Finish", LocalTime.of(12, 14, 51))
        )
        expected["243"] = listOf(
            Split("1km", LocalTime.of(12, 6, 15)),
            Split("2km", LocalTime.of(12, 10, 36)),
            Split("Finish", LocalTime.of(12, 14, 51))

        )
        assertEquals(expected, actual.mapValues { it.value.list })
    }

    @Test
    fun testOfSplitsInputBySplitsName() {
        val actual = splitsInput(BySplitsName, "src/test/resources/splitsByName")
        val expected = mutableMapOf<String, List<Split>>()
        expected["007"] = listOf(
            Split("1km", LocalTime.of(12, 7, 15)),
            Split("2km", LocalTime.of(12, 11, 36)),
            Split("3km", LocalTime.of(12, 13, 29)),
            Split("Finish", LocalTime.of(12, 14, 51))
        )
        expected["243"] = listOf(
            Split("1km", LocalTime.of(12, 6, 15)),
            Split("2km", LocalTime.of(12, 10, 36)),
            Split("Finish", LocalTime.of(12, 14, 51))

        )
        assertEquals(expected, actual.mapValues { it.value.list })
    }

    @Test
    fun getNeededPath() {
        val actual = getMapDistanceNameToNeededPath("src/test/resources/getNeededPathTestData.csv")
        val expected = mutableMapOf<String, NeededPath>()
        expected["1"] = NeededPath(
            listOf(
                PathSingleton(1, listOf("11")),
                PathSingleton(1, listOf("12")),
                PathSingleton(1, listOf("13")),
                PathSingleton(1, listOf("14")),
                PathSingleton(1, listOf("15")),
                PathSingleton(1, listOf("16")),
                )
        )
        expected["2"] = NeededPath(
            listOf(
                PathSingleton(1, listOf("21")),
                PathSingleton(1, listOf("w22", "w23")),
                PathSingleton(2, listOf("24t", "25t", "26t", "27t", "28t")),
                PathSingleton(1, listOf("29")),
                )
        )
        expected["3"] = NeededPath(listOf())
        assertEquals(expected, actual)
    }


}