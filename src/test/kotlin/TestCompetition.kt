import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*
import java.time.LocalTime
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Test1 {
    @Test
    fun testSaveAndRecreateSavedCompetition() {
        val targetPath = createTempDirectory().toString()
        val pathEvent = "sample-data/event.csv"
        val pathApplications = "sample-data/applications"
        val comp = makeCompetition(pathEvent, targetPath, pathApplications)
        createStartProtocols(targetPath, comp)
        saveCompetition(targetPath, comp)

        val comp1 = recreateSavedCompetition(targetPath)

        assertEquals(comp, comp1)
    }

    @Test
    fun testMakeCompetition() {
        val comp = makeCompetition("src/test/resources/event.csv", pathApplications = "sample-data/applications")
        assertEquals("Первенство пятой бани", comp.name)
        assertEquals(LocalDate(2022, 1, 1), comp.date)
    }

    @Test
    fun testTimeDifference() {
        val time1 = LocalTime.of(1, 0, 0)
        val time2 = LocalTime.of(4, 37, 0)
        val time3 = LocalTime.of(17, 28, 15)
        assertEquals(LocalTime.of(3, 37, 0), timeDifference(time1, time2))
        assertEquals(LocalTime.of(16, 28, 15), timeDifference(time1, time3))
        assertEquals(LocalTime.of(12, 51, 15), timeDifference(time2, time3))
    }

    @Test
    fun testRatioOfTwoTimes() {
        val time1 = LocalTime.of(1, 0, 0)
        val time2 = LocalTime.of(4, 37, 0)
        val time3 = LocalTime.of(17, 28, 15)
        val time4 = LocalTime.of(10, 0, 0)
        assertEquals(277.toDouble() / 60, ratioOfTwoTimes(time2, time1))
        assertEquals(1.toDouble(), ratioOfTwoTimes(time3, time3))
        assertEquals(10.toDouble(), ratioOfTwoTimes(time4, time1))
    }
}




