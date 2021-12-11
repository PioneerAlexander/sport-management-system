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

var state = State.ZERO

fun main() = application {
    Window(onCloseRequest = ::exitApplication,
            title = "ЭСПСС (Электронная система проведения спортивных соревнований)",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            icon = BitmapPainter(useResource("Bragilevsky2.ico", ::loadImageBitmap)), //своя иконка
            resizable = false)
    {
        when (state) {
            State.ZERO -> ZeroState()
            else -> ZeroState()
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
fun ZeroState() {
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp,100.dp)) {
        Button(onClick = { state = State.DOCS },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(200.dp).height(50.dp),
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
        Button(onClick = { state = State.DOCS },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(200.dp).height(50.dp),
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
        Button(onClick = { state = State.DOCS },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(200.dp).height(50.dp),
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
}

@Composable
fun DocsState() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { state = State.DOCS },
                modifier = Modifier.align(Alignment.CenterHorizontally),

                colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
        )

        {
            Text(
                    modifier = Modifier.align(Alignment.Top),
                    text = "Документация"
            )
        }
    }
}


