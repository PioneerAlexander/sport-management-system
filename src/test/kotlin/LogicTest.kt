import ru.emkn.kotlin.sms.*
import java.time.LocalTime
import kotlin.test.*

internal class TestOfInput1 {
    @Test
    fun doesSuitsPathTest(){
        val a = NeededPath(listOf(
            PathSingleton(1, listOf("1")),
            PathSingleton(1, listOf("2")),
            PathSingleton(1, listOf("3", "4")),
        ))
        val t1 = ActualPath(listOf(
            Split("1", LocalTime.of(0, 0, 0)),
            Split("2", LocalTime.of(0, 0, 0)),
            Split("3", LocalTime.of(0, 0, 0)),
        ))
        val t2 = ActualPath(listOf(
            Split("1", LocalTime.of(0, 0, 0)),
            Split("2", LocalTime.of(0, 0, 0)),
            Split("4", LocalTime.of(0, 0, 0)),
        ))
        val t3 = ActualPath(listOf(
            Split("1", LocalTime.of(0, 0, 0)),
            Split("4", LocalTime.of(0, 0, 0)),
            Split("3", LocalTime.of(0, 0, 0)),
        ))
        assertEquals(true, a.doesSuits(t1))
        assertEquals(true, a.doesSuits(t2))
        assertEquals(false, a.doesSuits(t3))
    }
}