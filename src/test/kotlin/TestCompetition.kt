import kotlinx.datetime.LocalDate
import ru.emkn.kotlin.sms.*
import java.io.File
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
        val comp = makeCompetition(File(pathEvent), targetPath, File(pathApplications).listFiles().map { it!! })
        createStartProtocols(targetPath, comp)
        saveCompetition(targetPath, comp)
        //println(File(targetPath).listFiles().map { it.name })
        //println(File("$targetPath/data.csv").isFile)
        val comp1 = recreateSavedCompetition(File("$targetPath/data.csv"))

        assertEquals(comp.participants, comp1.participants)
    }

    @Test
    fun testMakeCompetition() {
        val comp = makeCompetition(File("src/test/resources/event.csv"), applications = File("sample-data/applications").listFiles().map { it!! })
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




