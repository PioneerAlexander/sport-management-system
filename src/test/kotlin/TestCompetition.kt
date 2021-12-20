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
}




