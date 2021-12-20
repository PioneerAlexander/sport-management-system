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
}




