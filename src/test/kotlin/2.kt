import ru.emkn.kotlin.sms.makeCompetition
import ru.emkn.kotlin.sms.recreateSavedCompetition
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Test1 {
    @Test
    fun testRecreateSavedCompetition() {
        val targetPath = createTempDirectory().toString()
        val pathEvent = "sample-data/event.csv"
        val pathApplications = "sample-data/applications"
        val comp = makeCompetition(pathEvent, targetPath)
        comp.addOrganisationsToCompetition(pathApplications)
        comp.createStartProtocols()
        comp.save(targetPath)

        val comp1 = recreateSavedCompetition(targetPath)

        assertEquals(comp, comp1)
    }
}


