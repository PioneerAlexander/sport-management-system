package graphics

import androidx.compose.runtime.mutableStateOf
import java.io.File

object Files {
    val directory = mutableStateOf("")
    var isStartProtMade = mutableStateOf(true)
    var startList = mutableStateOf(listOf(listOf<String>()))
    val event = mutableStateOf(listOf<File>())
    val gotEvent = mutableStateOf(false)
    val applications = mutableStateOf(listOf<File>())
    val gotApplications = mutableStateOf(false)
    val saved = mutableStateOf(listOf<File>())
    val loadingSaved = mutableStateOf(false)
    val splits = mutableStateOf(listOf<File>())
    val gotSplits = mutableStateOf(false)
    val courses = mutableStateOf(listOf<File>())
    val gotCourses = mutableStateOf(false)
    val classes = mutableStateOf(listOf<File>())
    val gotClasses = mutableStateOf(false)
    val madeStartProtocols = mutableStateOf(false)
}