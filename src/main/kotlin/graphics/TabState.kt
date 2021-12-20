package graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import myDB.CompetitionsT
import myDB.ParticipantsT
import myDB.recreateCompetition
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.emkn.kotlin.sms.Competition
import ru.emkn.kotlin.sms.recreateSavedCompetition

enum class TabState {
    GROUPS, PARTICIPANTS, DISTANCES, TEAMS, NOTEOFPARTICIPANTS
}

@Composable
fun participantTableCreate():  MutableList<MutableList<MutableState<String>>> {
    val participantList = mutableListOf(mutableListOf(mutableStateOf("")))
    recreateCompetition().participants.forEach {
        participantList.add(
            mutableListOf(
                mutableStateOf(it.name),
                mutableStateOf(it.surname),
                mutableStateOf(it.ageGroup),
                mutableStateOf(it.birthYear),
                mutableStateOf(it.sportsCategory),
                mutableStateOf(it.organisation),
                mutableStateOf(it.startNumber),
                mutableStateOf(it.startTime.hour.toString()),
                mutableStateOf(it.startTime.minute.toString()),
                mutableStateOf(it.startTime.second.toString())
            )
        )
    }
    return participantList
}


@Composable
fun ListsState(state: MutableState<State>): State {
    val tabs =
        listOf(//Icons.Rounded.ArrowBack to State.ZERO,
            Icons.Default.Home to State.ZERO,
            Icons.Rounded.List to TabState.DISTANCES,
            Icons.Filled.Person to TabState.PARTICIPANTS,
            Icons.Rounded.AccountBox to TabState.GROUPS,
            Icons.Rounded.AddCircle to TabState.TEAMS,
            Icons.Rounded.FavoriteBorder to TabState.NOTEOFPARTICIPANTS
        )
    Column {
        val tabState = remember { mutableStateOf(0) }
        TabRow(selectedTabIndex = tabState.value, divider = {}, modifier = Modifier.height(50.dp)) {
            tabs.withIndex().forEach() { (index, tab) ->
                Tab(
                    icon = { Icon(tab.first, "") },
                    selected = tabState.value == index,
                    onClick = {
                        tabState.value = index
                    })
                when (index) {
                    0 -> state.value = State.ZERO
                    2 -> Table(participantTableCreate()).show()
                }
            }
        }
    }

    return state.value
}

