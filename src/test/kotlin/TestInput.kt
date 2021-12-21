import ru.emkn.kotlin.sms.*
import java.io.File
import java.time.LocalTime
import kotlin.test.*

internal class TestOfInput {
    @Test
    fun testGetSportClasses() {
        val actual = getSportClasses(File("src/test/resources/test1_classes.csv"))
        val expected = mapOf(
            Pair("1", Distance("МЖ9 10")),
            Pair("2", Distance("МЖ9 10"))
        )
        assertEquals(expected, actual)
    }




    @Test
    fun getNeededPath() {
        val actual = getMapDistanceNameToNeededPath(File("src/test/resources/getNeededPathTestData.csv"))
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

    @Test
    fun newSplitsInputTest1() {
        val actual = splitsInputNew(File("src/test/resources/splitsByNameNewFormat").listFiles().map { it!! })
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
    fun newSplitsInputTest2() {
        val actual = splitsInputNew(File("src/test/resources/splitsByNumNewFormat").listFiles().map { it!! })
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
    fun newSplitsInputTest3() {
        val actual = splitsInputNew(File("src/test/resources/multyTypeSplitsInput").listFiles().map { it!! })
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


}