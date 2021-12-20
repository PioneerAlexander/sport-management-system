import ru.emkn.kotlin.sms.*
import java.io.File
import java.time.LocalTime
import kotlin.test.*

fun startOnClTest(eventFile: File, applications: List<File>, folder: String): Int {
    val competition = makeCompetition(eventFile, folder, applications)
    createStartProtocols(folder, competition)
    println(competition.participants.count())
    myDB.saveCompetition(competition)
    return competition.participants.size
}

internal class DBtests {


    @Test
    fun lightTest() {
        val num = startOnClTest(
            File("sample-data/event.csv"),
            File("sample-data/applications").listFiles().map { it!! },
            "loli"
        )
        val competition = myDB.recreateCompetition()
        assertEquals(num, competition.participants.size)
        assertEquals(num, myDB.recreateCompetition().participants.size)
        println(competition.orgs.size)
        myDB.clean()
    }
}

