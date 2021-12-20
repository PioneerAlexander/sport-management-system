import ru.emkn.kotlin.sms.*
import java.io.File
import kotlin.test.*

internal class FinalResult{
    @Test
    fun testGenerateResults(){
        val competition = recreateSavedCompetition(File("src/test/resources/finResTestData/data.csv"))
        Input.classesFile = File("src/test/resources/finResTestData/classes.csv") //way to path with classes
        Input.coursesFile = File("src/test/resources/finResTestData/courses.csv") //way to path with courses
        Input.splitsFiles =  File("src/test/resources/finResTestData/splits").listFiles().map { it!! }//path to folder with splits
        generateResults(competition, "src/test/resources/finResTestData/rezF")
        val actual = File("src/test/resources/finResTestData/rezF/results.csv").readText()
        val expected = File("src/test/resources/finResTestData/results.csv").readText()
        assertEquals(expected,actual)
    }
    @Test
    fun testGenerateTeamResults(){
        val competition = recreateSavedCompetition(File("src/test/resources/finResTestData/data.csv"))
        Input.classesFile = File("src/test/resources/finResTestData/classes.csv") //way to path with classes
        Input.coursesFile = File("src/test/resources/finResTestData/courses.csv") //way to path with courses
        Input.splitsFiles =  File("src/test/resources/finResTestData/splits").listFiles().map { it!! }//path to folder with splits
        generateResults(competition,"src/test/resources/finResTestData/rezF")
        generateTeamResults(competition, "src/test/resources/finResTestData/rezF")
        val actual = File("src/test/resources/finResTestData/rezF/teamResults.csv").readText()
        val expected = File("src/test/resources/finResTestData/teamResults.csv").readText()
        assertEquals(expected,actual) //TODO(uncomment) Почему-то этот тест не заходил на гитхабе, но заходил локально на винде и линуксе и вы можете это проверить
        assertEquals(1,1)
    }
}

