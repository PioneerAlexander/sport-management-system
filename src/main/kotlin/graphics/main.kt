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
import java.awt.FileDialog
import java.io.File


enum class State {
    ZERO, DOCS, IMPORT, FINAL
}


object Paths {
    var pathDirectory = mutableStateOf("")
    var pathEvent: String = ""
    var pathApplications: String = ""
}





@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val windowState = remember { mutableStateOf(State.ZERO) }
    val tabState = remember { mutableStateOf(0) }

    val composeTable = mutableListOf(
        mutableListOf(
            mutableStateOf("a"),
            mutableStateOf("a"),
            mutableStateOf("a"),
            mutableStateOf("a")
        ),

        mutableListOf(
            mutableStateOf("a"),
            mutableStateOf("a"),
            mutableStateOf("a"),
            mutableStateOf("a")
        )
    )

    val table = Table(composeTable)

    Window(
        onCloseRequest = ::exitApplication,
        title = "ЭСПСС (Электронная система проведения спортивных соревнований)",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        icon = BitmapPainter(useResource("Bragilevsky2.ico", ::loadImageBitmap)), //своя иконка
        resizable = true,
    )
    {

//        val tabs =
//            listOf(Icons.Rounded.ArrowBack,Icons.Default.Home,Icons.Rounded.List, Icons.Filled.Person, Icons.Rounded.AccountBox,Icons.Rounded.AddCircle, Icons.Rounded.FavoriteBorder)
//
//        Column {
//            TabRow(selectedTabIndex = tabState.value, divider = {}, modifier = Modifier.height(50.dp)) {
//                tabs.withIndex().forEach() { (index, icon) ->
//                    Tab(
//                        icon = {Icon(icon, "")},
//                        selected = tabState.value == index,
//                        onClick = { tabState.value = index })
//                }
//            }
//        }

        //NewFileButton(FileDialog(ComposeWindow()))

        table.show(mutable=true)


//        windowState.value = when (windowState.value) {
//            State.ZERO -> ZeroState(windowState)
//            //State.IMPORT -> ImportState(windowState)
//            else -> ZeroState(windowState)
//        }
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
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp)) {
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
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { state.value = State.IMPORT },
            modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )
        {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Ввод",
                color = Color.White,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { state.value = State.ZERO },
            modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )
        {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Вывод",
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

//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun ImportState(state: MutableState<State>): State {
//    Column(modifier = Modifier.fillMaxWidth().offset(0.dp,100.dp)) {
//        TextField(
//                modifier = Modifier.onPreviewKeyEvent {
//                    (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
//                }.align(Alignment.CenterHorizontally),
//                value = Paths.pathDirectory,
//                onValueChange = { Paths.pathDirectory = it },
//                placeholder = { Text("Папка для сохранения.") }
//        )
//        Spacer(modifier = Modifier.height(5.dp))
//        TextField(
//                modifier = Modifier.onPreviewKeyEvent {
//                    (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
//                }.align(Alignment.CenterHorizontally),
//                value = Paths.pathEvent,
//                onValueChange = { Paths.pathEvent = it },
//                placeholder = { Text("Файл соревнования.") }
//        )
//        Spacer(modifier = Modifier.height(5.dp))
//        TextField(
//                modifier = Modifier.onPreviewKeyEvent {
//                    (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
//                }.align(Alignment.CenterHorizontally),
//                value = Paths.pathApplications,
//                onValueChange = { Paths.pathApplications = it },
//                placeholder = { Text("Папка с заявками.") }
//        )
//    }
//    return state.value
//
// }



//NewFileButton(FileDialog(ComposeWindow()))
@Composable
fun NewFileButton(fileDialog: FileDialog) {
    val text: String = "Загрузить файлы"
    val files = mutableListOf<File>()

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        onClick = {
            fileDialog.isMultipleMode = true
            fileDialog.isVisible = true
            files += fileDialog.files
            //count.value = files.size
        }
    ) {
        Text(text)
    }
}
