import ru.emkn.kotlin.sms.Participant
import ru.emkn.kotlin.sms.Split
import ru.emkn.kotlin.sms.getMapFromNumberToSplits
import java.time.LocalTime
import kotlin.test.*

internal class Test1 {
    @Test
    fun testOfGetMapFromNumberToSplits(){
        val a = getMapFromNumberToSplits("src/test/resources/test1.csv")
        val b = mutableMapOf<String, List<Split>>()
        b["1"] = listOf(
            Split("2", LocalTime.of(12, 0, 0)),
            Split("3", LocalTime.of(12, 10, 10))
        )
        assertEquals(b, a)
    }
}
