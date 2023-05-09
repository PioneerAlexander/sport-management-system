package graphics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CheckpointsState(state: MutableState<State>): State {
    Button(onClick = { state.value = State.ZERO }) { Text(text = "Back", color = Color.White) }
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {

        NewFileButton(
            "CLASSES",
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