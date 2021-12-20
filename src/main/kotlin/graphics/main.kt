package graphics

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.ContextMenuDataProvider
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import graphics.Files.loadingSaved
import myDB.recreateCompetition
import ru.emkn.kotlin.sms.recreateSavedCompetition
import ru.emkn.kotlin.sms.startOnCl
import java.awt.FileDialog
import java.io.File


enum class State {
    ZERO, DOCS, IMPORT, FINAL, START_PROTOCOLS, CHECKPOINTS, LISTS
}

object Tables {

    var tableCreateParticipant = mutableListOf(mutableListOf(mutableStateOf("")))
    var startProtocolsTable = Table(mutableListOf(mutableListOf(mutableStateOf(""))))
    var finishProtocolsTable = Table(mutableListOf(mutableListOf(mutableStateOf(""))))
    var splitsType1 = Table(mutableListOf(mutableListOf(mutableStateOf(""))))
    var spitsType2 = Table(mutableListOf(mutableListOf(mutableStateOf(""))))
    var groupTable = Table(mutableListOf(mutableListOf(mutableStateOf(""))))

}

object Files {
    var directory = mutableStateOf("")
    var gotDirectory = mutableStateOf(false)
    var event = mutableStateOf(listOf<File>())
    var gotEvent = mutableStateOf(false)
    var applications = mutableStateOf(listOf<File>())
    var gotApplications = mutableStateOf(false)
    var saved = mutableStateOf(listOf<File>())
    var loadingSaved = mutableStateOf(false)
    var splits = mutableStateOf(listOf<File>())
    var gotSplits = mutableStateOf(false)
    var courses = mutableStateOf(listOf<File>())
    var gotCourses = mutableStateOf(false)
    var classes = mutableStateOf(listOf<File>())
    var gotClasses = mutableStateOf(false)
}


@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val windowState = remember { mutableStateOf(State.ZERO) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "ЭСПСС (Электронная система проведения спортивных соревнований)",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        icon = BitmapPainter(useResource("Bragilevsky2.ico", ::loadImageBitmap)), //своя иконка
        resizable = true,
    )
    {
        //Table(mutableListOf(mutableListOf(mutableStateOf("")))).show()
        windowState.value = when (windowState.value) {
            State.ZERO -> ZeroState(windowState)
            State.IMPORT -> ImportState(windowState)
            State.LISTS -> ListsState(windowState)
            State.CHECKPOINTS -> CheckpointsState(windowState)
            else -> ZeroState(windowState)
        }
    }
}

@Composable
fun TextBox(text: String = "Item", color: String = "grey", fontSize: TextUnit = 10.sp) {
    Box(
        modifier = Modifier
            .width(400.dp)
            .background(
                color = when (color) {
                    "grey" -> Color(100, 100, 100, 50)
                    "red" -> Color(200, 0, 0, 30)
                    "green" -> Color(0, 200, 0, 30)
                    else -> Color(100, 100, 100, 35)
                }
            )
            .padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        SelectionContainer {
            Text(text = text, fontSize = fontSize)
        }
    }
}

@Composable
fun ZeroState(state: MutableState<State>): State {
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Button(
            onClick = { state.value = State.DOCS },
            modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )
        {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Документация",
                color = Color.White,
                fontSize = 20.sp
            )
        }
        Button(
            onClick = { state.value = State.IMPORT },
            modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )
        {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Создать/загрузить",
                color = Color.White,
                fontSize = 20.sp
            )
        }
        if (Files.loadingSaved.value || (Files.gotApplications.value && Files.gotEvent.value)) {
            Button(
                onClick = {
                    state.value = State.START_PROTOCOLS
                    if (Files.loadingSaved.value) {
                        recreateSavedCompetition(Files.saved.value.first())
                    } else {
                        startOnCl(Files.event.value.first(), Files.applications.value, Files.directory.value)
                    }
                    //TODO("Генерация стартовых протоколов")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
            ) {
                Text(
                    text = "Стартовые протоколы",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
        Button(
            onClick = {
                state.value = State.CHECKPOINTS
            },
            modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )
        {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Данные соревнования",
                color = Color.White,
                fontSize = 20.sp
            )
        }
        Button(
            onClick = {
                state.value = State.FINAL
                TODO("implement final results (by teams?)")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )
        {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Результаты",
                color = Color.White,
                fontSize = 20.sp
            )
        }
        Button(
            onClick = {
                state.value = State.LISTS
                Tables.tableCreateParticipant = participantTableCreate()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )
        {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Списки",
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
    Row(modifier = Modifier.fillMaxHeight())
    {
        Column(modifier = Modifier.fillMaxWidth().align(Alignment.Bottom)) {
            Text(
                modifier = Modifier.align(Alignment.End),
                text = "© unnamed team",
                color = Color.Black,
                fontSize = 10.sp
            )
        }
    }

    return state.value
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImportState(state: MutableState<State>): State {
    Button(onClick = { state.value = State.ZERO }) { Text(text = "Назад", color = Color.White) }
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {

        TextField(
            modifier = Modifier.onPreviewKeyEvent {
                (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
            }.align(Alignment.CenterHorizontally),
            value = Files.directory.value,
            onValueChange = { Files.directory.value = it },
            placeholder = { Text("Имя папки для сохранения.") }
        )
        NewFileButton("Файл соревнования", Modifier.align(Alignment.CenterHorizontally).width(300.dp), {
            Files.gotEvent.value = true
        }, type = InputFilesType.EVENT)
        NewFileButton(
            "Все файлы заявок",
            Modifier.align(Alignment.CenterHorizontally).width(300.dp),
            {
                Files.gotApplications.value = true
            },
            type = InputFilesType.APPLICATIONS
        )


        NewFileButton(
            "Загрузить сохраненное",
            Modifier.align(Alignment.CenterHorizontally).width(300.dp),
            {
                Files.loadingSaved.value = true
                state.value = State.ZERO
            },
            type = InputFilesType.SAVED
        )
    }
    return state.value

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CheckpointsState(state: MutableState<State>): State {
    Button(onClick = { state.value = State.ZERO }) { Text(text = "Назад", color = Color.White) }
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {

        NewFileButton("CLASSES",
            Modifier.align(Alignment.CenterHorizontally).width(300.dp),
            {
                Files.gotClasses.value = true
            },
            type = InputFilesType.CLASSES
        )

        NewFileButton(
            "COURSES",
            Modifier.align(Alignment.CenterHorizontally).width(300.dp),
            {
                Files.gotCourses.value = true
            },
            type = InputFilesType.COURSES
        )


        NewFileButton(
            "SPLITS",
            Modifier.align(Alignment.CenterHorizontally).width(300.dp),
            {
                Files.gotSplits.value = true
            },
            type = InputFilesType.SPLITS
        )
    }
    return state.value

}


//NewFileButton(FileDialog(ComposeWindow()))
@Composable
fun NewFileButton(text: String, modifier: Modifier, onClick: () -> Unit = {}, type: InputFilesType) {
    val fileDialog = FileDialog(ComposeWindow())

    val files = mutableListOf<File>()
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128)),
        onClick = {
            fileDialog.isMultipleMode = true
            fileDialog.isVisible = true
            files += fileDialog.files
            when (type) {
                InputFilesType.EVENT -> Files.event.value = files
                InputFilesType.APPLICATIONS -> Files.applications.value = files
   InputFilesType.SAVED -> Files.saved.value = files
                InputFilesType.COURSES -> Files.courses.value = files
                InputFilesType.CLASSES -> Files.classes.value = files
                InputFilesType.SPLITS -> Files.splits.value = files

            }
            onClick()
        }
    ) {
        Text(text, color = Color.White)
    }
}


enum class InputFilesType {
    EVENT, APPLICATIONS, SAVED, COURSES, CLASSES, SPLITS, OTHER
}