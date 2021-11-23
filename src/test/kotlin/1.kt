import ru.emkn.kotlin.sms.*
import java.time.LocalTime
import kotlin.test.*

internal class TestOfInput {
    @Test
    fun testOfGetMapFromNumberToSplits(){
        val a = getMapFromNumberToSplits("src/test/resources/test1.csv")
        val b = mutableMapOf<String, List<Split>>()
        b["1"] = listOf(
            Split("2", LocalTime.of(12, 0, 0)),
            Split("3", LocalTime.of(12, 10, 10))
        )
        assertEquals(b, a)
    }

    @Test
    fun testGetSportClasses(){
        val a = getSportClasses("src/test/resources/test1_classes.csv")
        val b = mapOf<String, Distance>(
            Pair("1", Distance("МЖ9 10")),
            Pair("2", Distance("МЖ9 10"))
        )
        assertEquals(b, a)
    }

    @Test
    fun testGetMapOfDistancesCheckpoints(){
        val a = getMapOfDistancesCheckpoints("src/test/resources/test1_courses.csv")
        val b = mapOf<String, List<String>>(
            Pair("1", listOf("11", "12", "13", "14", "15", "16")),
            Pair("2", listOf("21", "22", "23", "24", "25", "26", "27", "28", "29")),
            Pair("3", listOf())
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

