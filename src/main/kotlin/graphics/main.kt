package graphics

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.ContextMenuDataProvider
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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


enum class State {
    ZERO, DOCS, IMPORT, FINAL
}


var pathDirectory = ""
var pathEvent = ""
var pathApplications = ""




fun main() = application {
    var state = remember { mutableStateOf(State.ZERO) }
    Window(onCloseRequest = ::exitApplication,
            title = "ЭСПСС (Электронная система проведения спортивных соревнований)",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            icon = BitmapPainter(useResource("Bragilevsky2.ico", ::loadImageBitmap)), //своя иконка
            resizable = true)
    {
        state = when (state.value) {
            State.ZERO -> ZeroState(state)
            State.IMPORT -> ImportState(state)
            else -> ZeroState(state)
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
fun ZeroState(state: MutableState<State>): MutableState<State> {
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp)) {
        Button(onClick = { state.value = State.DOCS },
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
        Button(onClick = { state.value = State.IMPORT },
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
        Button(onClick = { state.value = State.ZERO },
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
    return state
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImportState(state: MutableState<State>): MutableState<State> {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
                modifier = Modifier.onPreviewKeyEvent {
                    (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
                }.align(Alignment.CenterHorizontally),
                value = pathDirectory,
                onValueChange = { pathDirectory = it },
                placeholder = { Text("Папка для сохранения.") }
        )
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
                modifier = Modifier.onPreviewKeyEvent {
                    (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
                }.align(Alignment.CenterHorizontally),
                value = pathEvent,
                onValueChange = { pathEvent = it },
                placeholder = { Text("Файл соревнования.") }
        )
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
                modifier = Modifier.onPreviewKeyEvent {
                    (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
                }.align(Alignment.CenterHorizontally),
                value = pathApplications,
                onValueChange = { pathApplications = it },
                placeholder = { Text("Папка с заявками.") }
        )
    }
    return state
}


