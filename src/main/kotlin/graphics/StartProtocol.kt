package graphics

import androidx.compose.foundation.*
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
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalComposeUiApi::class)

@Composable
fun StartProtocol(state: MutableState<State>, data: List<List<String>>): State {

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
            .padding(10.dp)
    ) {
        val stateVertical = rememberScrollState(0)
        val stateHorizontal = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(stateHorizontal)
        ) {
            Column {
                data.forEach { listS ->
                    Row {
                        listS.forEach {
                            Text(
                                text = "$it,",
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(stateVertical)
        )
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 12.dp),
            adapter = rememberScrollbarAdapter(stateHorizontal)
        )
        Button(onClick = { state.value = State.ZERO }, modifier = Modifier.align(Alignment.TopEnd)) {
            Text(
                text = "x",
                color = Color.White
            )
        }

    }

    return state.value
}