import ru.emkn.kotlin.sms.*
import ru.emkn.kotlin.sms.InputTag.ByParticipantNum
import java.io.File
import kotlin.test.*

internal class FinalResult{
    @Test
    fun testGenerateResults(){
        ParticipantsPath.Companion
        val competition = recreateSavedCompetition("src/test/resources/finResTestData")
        Input.inputTag = ByParticipantNum
        Input.classesPath = "src/test/resources/finResTestData/classes.csv" //way to path with classes
        Input.coursesPath = "src/test/resources/finResTestData/courses.csv" //way to path with courses
        Input.splitsPath =  "src/test/resources/finResTestData/splits"//path to folder with splits
        generateResults(competition, "src/test/resources/finResTestData/rezF")
        val actual = File("src/test/resources/finResTestData/rezF/results.csv").readText()
        val expected = File("src/test/resources/finResTestData/results.csv").readText()
        assertEquals(expected,actual)
    }
    @Test
    fun testGenerateTeamResults(){
        val competition = recreateSavedCompetition("src/test/resources/finResTestData")
        Input.inputTag = ByParticipantNum
        Input.classesPath = "src/test/resources/finResTestData/classes.csv"
        Input.coursesPath = "src/test/resources/finResTestData/courses.csv"
        Input.splitsPath =  "src/test/resources/finResTestData/splits"
        generateResults(competition,"src/test/resources/finResTestData/rezF")
        generateTeamResults(competition, "src/test/resources/finResTestData/rezF")
        val actual = File("src/test/resources/finResTestData/rezF/teamResults.csv").readText()
        val expected = File("src/test/resources/finResTestData/teamResults.csv").readText()
        assertEquals(expected,actual) //TODO(uncomment) Почему-то этот тест не заходил на гитхабе, но заходил локально на винде и линуксе и вы можете это проверить
        assertEquals(1,1)
    }
}

