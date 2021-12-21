package graphics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.finalResOnCl


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
                finalResOnCl(
                    Files.classes.value.first(),
                    Files.courses.value.first(),
                    Files.splits.value,
                    Files.directory.value
                )
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
        if (Files.gotCourses.value && Files.gotSplits.value &&
            Files.gotClasses.value && Files.madeStartProtocols.value
        ) {
            Button(
                onClick = {
                    state.value = State.LISTS
                    Tables.tableCreateParticipant = participantTableCreate()
                    Tables.tableCreateSplits = splitsTableCreate()
                    Tables.tableCreateDistance = distanceTableCreate()
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