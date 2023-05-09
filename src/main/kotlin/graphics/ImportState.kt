package graphics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.startOnCl


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImportState(state: MutableState<State>): State {
    Button(onClick = { state.value = State.ZERO }) { Text(text = "Back", color = Color.White) }
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {

        TextField(
            modifier = Modifier.onPreviewKeyEvent {
                (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
            }.align(Alignment.CenterHorizontally),
            value = Files.directory.value,
            onValueChange = { Files.directory.value = it },
            placeholder = { Text("Folder name for saving results.") }
        )
        NewFileButton("Competition file", Modifier.align(Alignment.CenterHorizontally).width(300.dp), {
            Files.gotEvent.value = true
        }, type = InputFilesType.EVENT)
        NewFileButton(
            "All application files",
            Modifier.align(Alignment.CenterHorizontally).width(300.dp),
            {
                Files.gotApplications.value = true
            },
            type = InputFilesType.APPLICATIONS
        )
        if (Files.loadingSaved.value || (Files.gotApplications.value && (Files.gotEvent.value))) {
            if (Files.isStartProtMade.value){
                Files.startList = mutableStateOf(startOnCl(Files.event.value.first(), Files.applications.value, Files.directory.value))
                Files.isStartProtMade = mutableStateOf(false)
            }
            Button(
                onClick = {
                    state.value = State.START_PROTOCOLS
                    Files.madeStartProtocols.value = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
            ) {
                Text(
                    text = "Start protocols",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
    return state.value

}
