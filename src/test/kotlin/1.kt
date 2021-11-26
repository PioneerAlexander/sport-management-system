import ru.emkn.kotlin.sms.*
import java.time.LocalTime
import kotlin.test.*

internal class TestOfInput {
    @Test
    fun testOfGetMapFromNumberToSplits() {
        val a = getMapFromNumberToSplits("src/test/resources/test1.csv")
        val b = mutableMapOf<String, List<Split>>()
        b["1"] = listOf(
            Split("2", LocalTime.of(12, 0, 0)),
            Split("3", LocalTime.of(12, 10, 10))
        )
        assertEquals(b, a)
    }

    @Test
    fun testGetSportClasses() {
        val a = getSportClasses("src/test/resources/test1_classes.csv")
        val b = mapOf<String, Distance>(
            Pair("1", Distance("МЖ9 10")),
            Pair("2", Distance("МЖ9 10"))
        )
        assertEquals(b, a)
    }

    @Test
    fun testGetMapOfDistancesCheckpoints() {
        val a = getMapOfDistancesCheckpoints("src/test/resources/test1_courses.csv")
        val b = mapOf<String, List<String>>(
            Pair("1", listOf("11", "12", "13", "14", "15", "16")),
            Pair("2", listOf("21", "22", "23", "24", "25", "26", "27", "28", "29")),
            Pair("3", listOf())
        )
        assertEquals(b, a)
    }

    @Test
    fun testOfSplitsInputByParticipantName() {
        val a = splitsInputByParticipantNum("ByParticipantNum", "src/test/resources/splitsByNum")
        val b = mutableMapOf<String, List<Split>>()
        b["007"] = listOf(
            Split("1km", LocalTime.of(12, 7, 15)),
            Split("2km", LocalTime.of(12, 11, 36)),
            Split("3km", LocalTime.of(12, 13, 29)),
            Split("Finish", LocalTime.of(12, 14, 51))
        )
        b["243"] = listOf(
            Split("1km", LocalTime.of(12, 6, 15)),
            Split("2km", LocalTime.of(12, 10, 36)),
            Split("Finish", LocalTime.of(12, 14, 51))

        )
        assertEquals(b, a)
    }

    @Test
    fun testOfSplitsInputBySplitsName() {
        val a = splitsInputByParticipantNum("BySplitsName", "src/test/resources/splitsByName")
        val b = mutableMapOf<String, List<Split>>()
        b["007"] = listOf(
            Split("1km", LocalTime.of(12, 7, 15)),
            Split("2km", LocalTime.of(12, 11, 36)),
            Split("3km", LocalTime.of(12, 13, 29)),
            Split("Finish", LocalTime.of(12, 14, 51))
        )
        b["243"] = listOf(
            Split("1km", LocalTime.of(12, 6, 15)),
            Split("2km", LocalTime.of(12, 10, 36)),
            Split("Finish", LocalTime.of(12, 14, 51))

        )
        assertEquals(b, a)
    }

}
/*
internal class TestOfParticipant{
    @Test
    TODO()
}

 */

