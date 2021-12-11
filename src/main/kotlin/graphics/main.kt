package graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.MenuBar
import androidx.compose.material.DropdownMenuItem
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

val teams = listOf<String>("team1", "team2", "team3")

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "title", resizable = false) {
        val count = remember { mutableStateOf(0) }

        Column(modifier = Modifier.width(100.dp).absoluteOffset(100.dp,200.dp)){
            for (team in teams){
                TextBox(team, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }

        TextBox(count.value.toString())
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
