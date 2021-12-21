package graphics

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState


@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val windowState = remember { mutableStateOf(State.ZERO) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "ЭСПСС (Электронная система проведения спортивных соревнований)",
        state = rememberWindowState(width = 850.dp, height = 600.dp),
        icon = BitmapPainter(useResource("Bragilevsky2.ico", ::loadImageBitmap)), //своя иконка
        resizable = true,
    )
    {
        windowState.value = when (windowState.value) {
            State.DOCS -> PrintDock(windowState)
            State.ZERO -> ZeroState(windowState)
            State.IMPORT -> ImportState(windowState)
            State.LISTS -> ListsState(windowState)
            State.CHECKPOINTS -> CheckpointsState(windowState)
            State.START_PROTOCOLS -> StartProtocol(windowState, Files.startList.value)
            else -> ZeroState(windowState)
        }
    }
}



