import ru.emkn.kotlin.sms.*
import java.io.File
import java.time.LocalTime
import kotlin.test.*

internal class finalResult{
    @Test
    fun test(){
        val competition = recreateSavedCompetition("src/test/resources/finResTestData")
        competition.inputTag = "ByParticipantNum"
        competition.classesPath = "src/test/resources/finResTestData/classes.csv" //way to path with classes
        competition.coursesPath = "src/test/resources/finResTestData/courses.csv" //way to path with courses
        competition.splitsPath =  "src/test/resources/finResTestData/splits"//path to folder with splits
        generateResults(competition, "src/test/resources/finResTestData/rezF")
        val a = File("src/test/resources/finResTestData/rezF/results.csv").readText()
        val b = File("src/test/resources/finResTestData/results.csv").readText()
        assertEquals(b,a)
    }

}