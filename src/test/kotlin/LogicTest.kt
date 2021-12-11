import ru.emkn.kotlin.sms.*
import java.time.LocalTime
import kotlin.test.*

internal class TestOfInput1 {
    @Test
    fun doesSuitsPathTest(){
        val a = listOf(
            PathSingletons(1, listOf("1")),
            PathSingletons(1, listOf("2")),
            PathSingletons(1, listOf("3", "4")),
        )
        val t1 = listOf("1", "2", "3")
        val t2 = listOf("1", "2", "4")
        val t3 = listOf("1", "3", "2")
        assertEquals(true, a.doesSuitsPath(t1))
        assertEquals(true, a.doesSuitsPath(t2))
        assertEquals(false, a.doesSuitsPath(t3))


    }
}